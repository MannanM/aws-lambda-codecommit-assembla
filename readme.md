# aws-lambda-codecommit-assembla

## Automatic updating of tickets when a new commit is made

If you are using AWS's CodeCommit for your source control, you will often want to automatically update tickets or JIRA cards whena  new commit is made.

You can do this by creating a AWS Lambda that is triggered on a CloudCommit push. The lambda can retrieve the message and if provided with enough information, can automatically update the ticket for you.

## How does it works?

Firstly, the lambda accepts a `com.amazonaws.services.lambda.runtime.events.CodeCommitEvent` event (configured in the CLI below).

It then extracts the repository name and commit hash from this event, and then looks this up using the CodeCommit SDK.

The lambda has permission to do this, as it is using a role (`lambda-codecommit`) that has been granted `AWSCodeCommitReadOnly` permission.

With this information, it can get the commit message, which then uses regex to retrieve any tickets in the message i.e. `#1234`.

For each ticket found, it will call and update the ticket with a comment that contains the author, message and link to CodeTrail commit.

Additionally, if the ticket id has a word preceding it, e.g. `Test #1234`, it will also try and update the status of the ticket to "Test".
 
This should be able to be easily extended to use any ticket tool, like Jira, with minimal modifications.

## Instructions

- Update `build.gradle` SPACE, API_KEY and API_SECRET to match your Assembla configuration.
- Run `./gradlew createRole createLambda` - This will create an IAM role and upload the new Lambda to use the role
- To add a CodeCommit trigger, you can use the AWS Console or run the below CLI command, just substitute the <variables>

```bash
aws codecommit put-repository-triggers --repository-name <repository name> \
  --triggers name=Commit-Trigger,destinationArn=arn:aws:lambda:<region>:<account number>:function:codecommit-lambda,branches=[],events=updateReference
```

- If you want to update the comment or the regex matching, make the changes then run `./gradlew updateLambda` to push the latest code.