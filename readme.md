# aws-lambda-codecommit-push-lambda

## Automatic updating of tickets when a new commit is made

If you are using AWS's [CodeCommit](https://aws.amazon.com/codecommit/) for your source control, you will often want to automatically update tickets or cards when a new commit is made.
You can do this by creating a AWS Lambda function that is triggered when a CodeCommit push occurs.
The Lambda can then retrieve the commit message, and if provided with enough information, can automatically update the ticket for you.

## How does it works?

Firstly, the Lambda has a repository trigger as the event source (configured via the AWS CLI below) which provides `com.amazonaws.services.lambda.runtime.events.CodeCommitEvent` as the input data.
From this event source, it will extract the custom data from this trigger and expects it to be in the format `<workflow tool>:<board or space name>`.
For example, if you are using [Trello](https://trello.com), it could be `trello:nSdNt3FD` where `nSdNt3FD` is the board ID where the cards are.
If you were using [Assembla](https://www.assembla.com), you could use `assembla:your-space` where `your-space` is the name of the space where your tickets live.

From the push event itself, it extracts the repository name and commit hash, and then queries the CodeCommit Java SDK for more information.
The Lambda has permission to do this lookup, as it is using a role (`lambda-codecommit`) that has been granted `AWSCodeCommitReadOnly` permission.

With this new information, it can get the commit message, which then uses regex to retrieve any tickets mentioned in the message i.e. `#1234`.
For each ticket found, it will call and add a comment to the ticket that contains the author, commit message and link to CodeCommit console with more information about the commit.

Additionally, if the card/ticket has a word preceding it, e.g. `Test #1234`, it will also try and update the list/status of the ticket to "Test".
This should be able to be easily extended to use any other ticket tool, like Jira, with minimal modifications.

## Instructions

- Update `build.gradle` API_KEY and API_SECRET to match your [Assembla](https://app.assembla.com/user/edit/manage_clients) or [Trello](https://trello.com/app-key) configuration.
- Run `./gradlew createRole createLambda` - This will create an IAM role and upload the new Lambda to use the role
- To then add a CodeCommit repository trigger, you can do this via the AWS Console or run the below AWS CLI command, just remember to substitute all the `<variables>`

```bash
aws codecommit put-repository-triggers --repository-name <repository name> --triggers \
  name=Commit-Trigger,destinationArn=arn:aws:lambda:<region>:<account number>:function:codecommit-lambda,customData=<worktool>:<space>,branches=[],events=updateReference
```

- If you want to update the comment or the regex matching, make the Java changes then run `./gradlew updateLambda` to push the latest code.