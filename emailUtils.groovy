def sendEmail(subjectPrefix, buildUrl, projectName, buildNumber, changes, buildLog, recipient) {
    def emailBody = """
        <html>
            <body>
                <h2>Jenkins Build Report</h2>
                <p>Project: ${projectName}</p>
                <p>Build Number: ${buildNumber}</p>
                <p><a href="${buildUrl}">View Build Results</a></p>
                <pre>${changes}</pre>
                <p>Build Log:</p>
                <pre>${buildLog}</pre>
            </body>
        </html>
    """
    
    emailext(
        body: emailBody,
        mimeType: 'text/html',
        to: recipient,
        subject: "${subjectPrefix} ${projectName} - #${buildNumber}"
    )
}
