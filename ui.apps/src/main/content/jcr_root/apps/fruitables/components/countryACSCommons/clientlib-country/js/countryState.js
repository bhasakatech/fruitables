$(document).on("foundation-contentloaded", function (e) {
    var $dialog = $(e.target);
    var $countryField = $dialog.find(".country-dropdown");
    var $stateField = $dialog.find(".state-dropdown");

    if (!$countryField.length || !$stateField.length) {
        return;
    }

    var $stateWrapper = $stateField.closest(".coral-Form-fieldwrapper");

    function toggleStateField(show) {
        if ($stateWrapper.length) {
            $stateWrapper.toggle(show);
        }
    }

    function clearAndInitStateDropdown(stateSelect) {
        stateSelect.items.clear();

        var placeholder = new Coral.Select.Item();
        placeholder.value = "";
        placeholder.content.textContent = "Select state";
        placeholder.selected = true;
        stateSelect.items.add(placeholder);
    }

    function populateStates(states) {
        var stateSelect = $stateField.get(0);

        if (!stateSelect) {
            return;
        }

        clearAndInitStateDropdown(stateSelect);

        states.forEach(function (item) {
            var option = new Coral.Select.Item();
            option.value = item.value || "";
            option.content.textContent = item.text || item.value || "";
            stateSelect.items.add(option);
        });
    }

    async function loadStates(country) {
        var path = "/bin/states?country=" + encodeURIComponent(country);
        var response = await fetch(path);

        if (!response.ok) {
            throw new Error("States API request failed");
        }

        return response.json();
    }

    toggleStateField(false);

    $countryField.off("change.countryState").on("change.countryState", async function () {
        var country = $countryField.val();

        if (!country) {
            var stateSelect = $stateField.get(0);
            if (stateSelect) {
                clearAndInitStateDropdown(stateSelect);
            }
            toggleStateField(false);
            return;
        }

        try {
            var states = await loadStates(country);
            populateStates(states || []);
            toggleStateField(true);
        } catch (error) {
            console.error("Unable to load states", error);
            toggleStateField(false);
        }
    });
});