(function () {
    "use strict";

    document.addEventListener("DOMContentLoaded", function () {

        fetch('/bin/harsha')
            .then(function (response) {
                return response.json();
            })
            .then(function (data) {

                var html = "<table border='1'>";
                html += "<tr><th>ID</th><th>Name</th><th>Email</th></tr>";

                data.forEach(function (user) {
                    html += "<tr>" +
                        "<td>" + user.id + "</td>" +
                        "<td>" + user.name + "</td>" +
                        "<td>" + user.email + "</td>" +
                        "</tr>"; 
                });

                html += "</table>";

                document.getElementById("userTable").innerHTML = html;
            })
            .catch(function (error) {
                console.error("Error fetching data:", error);
            });

    });

})();