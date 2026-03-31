function handleSearch(event) {
    event.preventDefault();

    const value = document.getElementById("searchInput").value;

    if (!value || value.trim() === "") {
        alert("Please enter a search term");
        return false;
    }

    console.log("Search value:", value);

    // Future API call here
    return true;
}