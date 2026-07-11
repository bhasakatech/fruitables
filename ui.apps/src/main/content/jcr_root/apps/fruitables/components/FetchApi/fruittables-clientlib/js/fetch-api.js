document.addEventListener("DOMContentLoaded", function () {

    fetch("/bin/fetch-api")
        .then(response => response.json())
        .then(data => {

            if (!data || data.length === 0) return;

            const headers = Object.keys(data[0]);
            const tableHead = document.getElementById("tableHead");
            const tableBody = document.getElementById("tableBody");

       
            headers.forEach(key => {
                const th = document.createElement("th");
                th.innerText = key;
                tableHead.appendChild(th);
            });

     
            data.forEach(item => {
                const row = document.createElement("tr");

                headers.forEach(key => {
                    const td = document.createElement("td");
                    td.innerText = item[key];
                    row.appendChild(td);
                });

                tableBody.appendChild(row);
            });
        })
        .catch(err => console.error("API Error:", err));

});