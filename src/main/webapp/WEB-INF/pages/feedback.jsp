<html ng-app="feedbackApp">
<head>
    <meta charset="utf8"/>
    <title>Feedback Administration</title>
    <link rel="stylesheet" href="https://netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.0/angular.js"></script>
    <script data-require="chart.js@1.0.2" data-semver="1.0.2" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.2/Chart.min.js"></script>
    <script src="js/feedback.js"></script>
    <link rel="stylesheet" href="css/feedback.css">
</head>

<body ng-controller="FeedbackCtrl">
<div>
    <table>
        <tr>
            <td height="20"></td>
        </tr>
        <tr>
            <td width="20"></td>
            <td valign="top">
                <form name="feedbackUpload" ng-submit="uploadFile()">
                    <input type="file" file-model="myFile" required placeholder="feedback file"/>
                    <input type="text" ng-model="peopleLead" required placeholder="people lead"/>
                    <input type="submit" value="Upload"/>
                </form>
            </td>
            <td width="20"></td>
            <td valign="top"><p>${userName} </p></td>
            <td valign="top"><a href="${logout}">Logout</a> </td>
        </tr>
    </table>
</div>

<!--div ng-controller="MainCtrl" class="container">
    <input type="file" on-read-file="loadFile($fileContent)" />
</div-->

<table>
    <tr>
        <td width="300">
            <table class="table">
                <tr ng-repeat="receiver in receivers | filter:search">
                    <td><a ng-href='#here' ng-click='loadFeedback(receiver.id)'> {{receiver.id}}</a></td>
                    <td><a ng-href='#here' ng-click='loadFeedback(receiver.id)'> {{receiver.name}}</a></td>
                </tr>
            </table>
        </td>
        <td valign="top">
            <h4>{{receiver}}</h4>
            <canvas id="myChart" width="700" height="700"></canvas>
        </td>
    </tr>
</table>


</body>
</html>

