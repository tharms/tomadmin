var app = angular.module('feedbackApp', []);

app.controller('FeedbackCtrl', function ($scope, $http) {

    $http.get('receiver').then(function (receiversResponse) {
        $scope.receivers = receiversResponse.data;
    });

    $scope.loadFeedback =function(receiverId) {
        $http.get('/feedback/'+receiverId)
            .success(function(response) {

                $scope.receiver = response.receiver;

                var ctx = document.getElementById("myChart").getContext("2d");
                var data = {
                    labels: ["1. Solution orientedness", "2. Strategic decisions", "3. Analytical", "4. Overview", "5. Priority setting", "6. Output quality/speed", "7. Ownership", "8. Implementation / Follow through", "9. Emotional stability", "10. Embraces change", "11. Self improvement", "12. Connects people", "13. Team focus", "14. Shares knowledge", "15. Feedback provision", "16. Team athmosphere", "17. Inspiration", "18. Challenger"],
                    datasets: [{
                        label: "Self",
                        fillColor: "rgba(220,220,220,0.2)",
                        strokeColor: "rgba(220,220,220,1)",
                        pointColor: "rgba(220,220,220,1)",
                        pointStrokeColor: "#fff",
                        pointHighlightFill: "#fff",
                        pointHighlightStroke: "rgba(220,220,220,1)",
                        data: response.selfRatings
                    }, {
                        label: "Lead",
                        fillColor: "rgba(151,187,205,0.2)",
                        strokeColor: "rgba(151,187,205,1)",
                        pointColor: "rgba(151,187,205,1)",
                        pointStrokeColor: "#fff",
                        pointHighlightFill: "#fff",
                        pointHighlightStroke: "rgba(151,187,205,1)",
                        data: response.leadRatings
                    }, {
                        label: "Multi",
                        fillColor: "rgba(151,220,151,0.2)",
                        strokeColor: "rgba(151,220,151,1)",
                        pointColor: "rgba(151,220,151,1)",
                        pointStrokeColor: "#fff",
                        pointHighlightFill: "#fff",
                        pointHighlightStroke: "rgba(151,220,151,1)",
                        data: response.multiRatings
                    }]
                };

                var myRadarChart = new Chart(ctx).Radar(data, {
                    scaleShowLabels : true,
                    multiTooltipTemplate:  "<%=datasetLabel%> : <%= value %>"
                });

            })
    };

    $scope.uploadFile = function () {
        var file = $scope.myFile;
        var reader = new FileReader();
        reader.onloadend = function(e){
            var data = e.target.result;
            $http.post('/feedback', {'feedback': data, 'peopleLead': $scope.peopleLead})
                .success(function (response) {
                    $scope.uploadResult = response
                    $http.get('/receiver').then(function (receiversResponse) {
                        $scope.receivers = receiversResponse.data;
                    });
                })
                .error(function (response) {
                    $scope.uploadResult = response
                });
        }
        reader.readAsText(file);
    };

});

app.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);