(function(document, $, Granite) {

    "use strict";

    $(document).on("click", ".asset-delete-metadata-btn", async function(e) {

        e.preventDefault();
        e.stopPropagation();
        e.stopImmediatePropagation();

        console.log("Custom Action Clicked");

        let paths = [];

        $(".foundation-selections-item").each(function() {

            const path = $(this).data("foundationCollectionItemId");

            if (path) {
                paths.push(path);
            }
        });

        console.log("Selected Paths:", paths);

        try {

            const tokenResponse = await fetch("/libs/granite/csrf/token.json");

            const tokenData = await tokenResponse.json();

            const csrfToken = tokenData.token;

            const formData = new URLSearchParams();

            paths.forEach(function(path) {
                formData.append("paths", path);
            });

            const response = await fetch("/bin/assetDeleteMetadata", {

                method: "POST",

                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "CSRF-Token": csrfToken
                },

                body: formData.toString()
            });

            const result = await response.text();

            console.log("Servlet Response:", result);

            Granite.UI.alert(
                "Success",
                result,
                "success"
            );

        } catch(error) {

            console.log(error);

            Granite.UI.alert(
                "Error",
                error.message,
                "error"
            );
        }

        return false;

    });

})(document, Granite.$, Granite);