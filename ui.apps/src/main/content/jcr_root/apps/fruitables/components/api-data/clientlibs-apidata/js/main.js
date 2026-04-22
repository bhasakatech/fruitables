(function () {

    function loadApiData() {

        console.log("API JS Loaded");

        fetch('/bin/api-data')
            .then(function (res) {
                return res.json();
            })
            .then(function (data) {

                console.log("API DATA:", data);

                var tables = document.querySelectorAll('.apiTable');

                tables.forEach(function (table) {

                    var tbody = table.querySelector('tbody');

                    if (!tbody) return;

                    var rows = '';

                    if (data.products && data.products.length > 0) {

                        data.products.forEach(function (item) {
                            rows += '<tr>' +
                                '<td>' + item.id + '</td>' +
                                '<td>' + item.title + '</td>' +
                                '</tr>';
                        });

                    } else {
                        rows = '<tr><td colspan="2">No data found</td></tr>';
                    }

                    tbody.innerHTML = rows;
                });

            })
            .catch(function (err) {

                console.error("Error:", err);

                document.querySelectorAll('.apiTable tbody')
                    .forEach(function (tbody) {
                        tbody.innerHTML =
                            '<tr><td colspan="2">Error loading data</td></tr>';
                    });
            });
    }

    // ✅ Ensure DOM is ready
    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", loadApiData);
    } else {
        loadApiData();
    }

})();