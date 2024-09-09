def getEmailBody(buildUrl, projectName, buildNumber, changes, buildLog) {
    return """
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
}
