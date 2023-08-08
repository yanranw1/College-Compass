let search_form = $("#search_form");
let browse_form2 = $("#browse_form2");
let browse_form1 = $("#browse_form1");
const select = document.getElementById('browse_way1');

const options = ['Public University', 'Private University', 'Liberal Arts College', 'Technical College', 'Trade School', 'Alchemy School', 'Arts School', 'Online', 'Institution', 'Coding Bootcamp', 'Reading Club', 'Writers College', 'Training Facility', 'Cult', 'Military', 'Waldorf', 'Law School', 'Medical School', 'Business School', 'Community College', 'Vocational School', 'Music College', 'Mime School'];

for (let i = 0; i < options.length; i++) {
    const option = document.createElement('option');
    option.value = options[i];
    option.textContent = options[i];
    select.appendChild(option);
}

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleSearchResult(resultDataString) {
    let resultDataJson = JSON.parse(JSON.stringify(resultDataString));

    console.log("handle search response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If search succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
        localStorage.setItem("lstLink",JSON.stringify('school_list.html?school=' + resultDataJson['school'] + "&location=" + resultDataJson['location'] + "&other=" + resultDataJson['other'] + "&order=" + resultDataJson['order'] + "&genre=" + resultDataJson['genre']+ "&pagenum="+"20"+ "&whichpage=0" + "&groupby=school" + "&distinct=true"));
        window.location.replace('school_list.html?school=' + resultDataJson['school'] + "&location=" + resultDataJson['location'] + "&other=" + resultDataJson['other'] + "&order=" + resultDataJson['order'] + "&genre=" + resultDataJson['genre']+ "&pagenum="+"20"+ "&whichpage=0" + "&groupby=school" + "&distinct=true");
    } else {
        // If search fails, the web page will display
        // error messages on <div> with id "search_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#search_error_message").text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitSearchForm(formSubmitEvent) {
    console.log("submit search form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/search", {
            method: "POST",
            // Serialize the search form to the data sent by POST request
            data: search_form.serialize(),
            success: handleSearchResult
        }
    );
}
function submitBrowseForm(event) {
    event.preventDefault();

    // Get the selected value from the dropdown menu
    const browseWayValue2 = $('#browse_way2').val();
    const browseWayValue1 = $('#browse_way1').val();



    // Construct the search query using the form data and the dropdown value
    const searchQuery = {
        browse_way2: browseWayValue2, browse_way1: browseWayValue1
        // Add other form fields here
    };
    // Send the search query to the server using AJAX
    $.ajax({
        type: 'POST',
        url: 'api/search',
        data: searchQuery,
        success: handleSearchResult,
        error: function(jqXHR, textStatus, errorThrown) {
            // Handle errors
            console.log("Browse request failed: " + textStatus + ", " + errorThrown);
        }
    });
}

// Bind the submit action of the form to a handler function
search_form.submit(submitSearchForm);
browse_form1.submit(submitBrowseForm);
browse_form2.submit(submitBrowseForm);























function handleLookup(query, doneCallback) {
    console.log("autocomplete initiated")
    // TODO: if you want to check past query results first, you can do it here


    var storedList = localStorage.getItem(query);
    if (storedList !== null) {
        console.log("using cached result")
        var parsedDictionary = JSON.parse(storedList);
        handleLookupAjaxSuccess(parsedDictionary, query, doneCallback)
    } else {
        console.log("Sending an ajax request to the server")
        jQuery.ajax({
            "method": "GET",
            // generate the request url from the query.
            // escape the query string to avoid errors caused by special characters
            "url": "school-suggestion?query=" + escape(query),
            "success": function(data) {
                // pass the data, query, and doneCallback function into the success handler
                handleLookupAjaxSuccess(data, query, doneCallback)
            },
            "error": function(errorData) {
                console.log("lookup ajax error")
                console.log(errorData)
            }
        })
    }
}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 *
 * data is the JSON data string you get from your Java Servlet
 *
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
    // console.log("lookup ajax successful")
    // parse the string into JSON
    var jsonData = JSON.parse(data);
    console.log(jsonData)

    // TODO: if you want to cache the result into a global variable you can do it here
    localStorage.setItem(query, JSON.stringify(data));

    // call the callback function provided by the autocomplete library
    // add "{suggestions: jsonData}" to satisfy the library response format according to
    //   the "Response Format" section in documentation
    doneCallback( { suggestions: jsonData } );
}


/*
 * This function is the select suggestion handler function.
 * When a suggestion is selected, this function is called by the library.
 *
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectResult(resultDataString) {
    let resultDataJson = JSON.parse(JSON.stringify(resultDataString));

    console.log("handle search response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If search succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
        localStorage.setItem("lstLink",JSON.stringify('search.html'));
        window.location.replace('single-school.html?id=' + resultDataJson['id']);
    } else {
        // If search fails, the web page will display
        // error messages on <div> with id "search_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#search_error_message").text(resultDataJson["message"]);
    }
}

function handleSelectSuggestion(suggestion) {
    // TODO: jump to the specific result page based on the selected suggestion
    console.log("you select " + suggestion["value"] + " with ID " + suggestion["data"]["heroID"])
    $.ajax(
        "api/search", {
            method: "POST",
            // Serialize the search form to the data sent by POST request
            data: {school_name: suggestion["value"] },
            success: handleSelectResult
        }
    );
}


/*
 * This statement binds the autocomplete library with the input box element and
 *   sets necessary parameters of the library.
 *
 * The library documentation can be find here:
 *   https://github.com/devbridge/jQuery-Autocomplete
 *   https://www.devbridge.com/sourcery/components/jquery-autocomplete/
 *
 */
// $('#autocomplete') is to find element by the ID "autocomplete"
setTimeout(function() {
    console.log("Autocomplete Initiated");
    $('#autocomplete').autocomplete({
        // documentation of the lookup function can be found under the "Custom lookup function" section
        lookup: function (query, doneCallback) {
            handleLookup(query, doneCallback)
        },
        onSelect: function(suggestion) {
            handleSelectSuggestion(suggestion)
        },
        // set delay time
        deferRequestBy: 300,
        // there are some other parameters that you might want to use to satisfy all the requirements
        // TODO: add other parameters, such as minimum characters
    });
    // console.log('Timeout completed!');
}, 300);


/*
 * do normal full text search if no suggestion is selected
 */
function handleNormalSearch(query) {
    console.log("doing normal search with query: " + query);
    // TODO: you should do normal search here
}

// bind pressing enter key to a handler function
setTimeout(function() {
    console.log("Autocomplete Initiated");
    $('#autocomplete').keypress(function(event) {
        // keyCode 13 is the enter key
        if (event.keyCode == 13) {
            // pass the value of the input box to the handler function
            handleNormalSearch($('#autocomplete').val())
        }
    })
    // console.log('Timeout completed!');
}, 300);







