# aws-lambda-codecommit-assembla

## Automatic updating of tickets when a new commit is made

If you are using AWS's CodeCommit for your source control, you will often want to automatically update tickets or JIRA cards whena  new commit is made.

You can do this by creating a AWS Lambda that is triggered on a CloudCommit push. The lambda can retrieve the message and if provided with enough information, can automatically update the ticket for you.