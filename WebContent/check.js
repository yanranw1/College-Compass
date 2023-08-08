let school_array = [];
let matchin_rate = [];
function handleSearchResult(student_data,resultData) {
    const genreSelection = student_data[2]
    const SATSelection = parseInt(student_data[0])
    const costSelection = parseInt(student_data[1])

    console.log(genreSelection, SATSelection, costSelection)

    let schoolTableBodyElement = jQuery("#match_table_body");
    for (let i = 0; i < resultData.length; i++) {
        console.log(i);
        let resultJSONData = JSON.parse(resultData[i])
        let matchscore = 0
        if(genreSelection==  resultJSONData["genre"]){
            matchscore += 2.5;
        }
        if(SATSelection >=  parseInt(resultJSONData["lower_SAT"])){
            matchscore += 2.5;
        }
        if(costSelection >= parseInt(resultJSONData["net_cost"])){
            matchscore += 2;
        }
        console.log("The result json array: " + resultData);
        console.log(resultJSONData["school_name"]);
        school_array.push( resultJSONData['school_id'] );
        matchin_rate.push(matchscore);
        let rowHTML = "";
        rowHTML += "<tr>";

        rowHTML += "<th>" + matchscore + "</th>";
        rowHTML +=
            "<th>" +
            // Add a link to single-school.html with id passed with GET url parameter
            '<a href="single-school.html?id=' + resultJSONData['school_id'] + '">'
            + resultJSONData["school_name"] +     // display school_name for the link text
            '</a>' +
            "</th>";
        rowHTML += "<th>" + resultJSONData["safety_level"] + "</th>";
        rowHTML +=
            "<th>" +
            // Add a link to single-school.html with id passed with GET url parameter
            '<a href="single-location.html?id=' + resultJSONData['location_id']+ '">'
            + resultJSONData["city"] +", "+ resultJSONData["state"] +    // display school_name for the link text
            '</a>' +
            "</th>";
        rowHTML += "<th>" + resultJSONData["lower_SAT"] + "</th>";
        rowHTML += "<th>" + resultJSONData["net_cost"] + "</th>";
        rowHTML += "</tr>";
        console.log(rowHTML);

        schoolTableBodyElement.append(rowHTML);
    }
    // localStorage.setItem("school_array",JSON.stringify(school_array))
    console.log("here");
    console.log(localStorage.getItem("user_id"));

    const user_id = JSON.parse(localStorage.getItem("user_id"));
    matchin_rate = JSON.stringify(matchin_rate);
    school_array = JSON.stringify(school_array);
    console.log(user_id);
    $.ajax({
        type: 'GET',
        url: 'api/check',
        data: {school_array: school_array, user_id: user_id, matching_rate: matchin_rate}
    });
}

console.log(1)
const student_data = JSON.parse(localStorage.getItem("student_data"));
const school_data = JSON.parse(localStorage.getItem("school_data"));

console.log(student_data)
console.log(school_data)
handleSearchResult(student_data,school_data)

