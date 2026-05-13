(function(document, $, Granite) {

    "use strict";

    $(document).on("foundation-selections-change", function() {

        const button = $(".assets-delete-action");

        button.off("click");

        button.on("click", async function(e) {

            e.preventDefault();
            e.stopPropagation();

            let paths = [];

            $(".foundation-selections-item").each(function() {

                const path = $(this).data("foundationCollectionItemId");

                console.log("Selected Path:", path);

                if (path) {
                    paths.push(path);
                }
            });

            console.log("All Selected Paths:", paths);

            if (paths.length === 0) {

                Granite.UI.alert(
                    "Error",
                    "No pages selected",
                    "error"
                );

                return;
            }

            try {

                // CSRF Token Fetch
                const tokenResponse = await fetch("/libs/granite/csrf/token.json");

                const tokenData = await tokenResponse.json();

                const csrfToken = tokenData.token;

                const formData = new URLSearchParams();

                paths.forEach(function(path) {
                    formData.append("paths", path);
                });

                const response = await fetch("/bin/assetsdelete", {

                    method: "POST",

                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                        "CSRF-Token": csrfToken
                    },

                    body: formData.toString()
                });

                console.log("Response Status:", response.status);

                if (!response.ok) {
                    throw new Error("Servlet Failed");
                }

                const result = await response.text();

                console.log("Servlet Success:", result);

                Granite.UI.alert(
                    "Success",
                    "Servlet Triggered Successfully",
                    "success"
                );

            } catch(error) {

                console.log("Fetch Error:", error);

                Granite.UI.alert(
                    "Error",
                    error.message,
                    "error"
                );
            }

        });

    });

})(document, Granite.$, Granite);