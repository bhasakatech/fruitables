
    function showMessage() {
        let firstNameValue = document.getElementById("firstName").value;
        let lastNameValue = document.getElementById("lastName").value;
        alert("First Name: " + firstNameValue + "\nLast Name: " + lastNameValue);
        let fullName = firstNameValue + " " + lastNameValue;
        alert("Full Name: " + fullName);
        document.getElementById("output").textContent = "Full Name: " + fullName;
         fetch("content/fruitables/us/ajaxCall.json")
        .then(res => res.text())
        .then(data => console.log(data))
        .catch(err => console.log(err));
    }
