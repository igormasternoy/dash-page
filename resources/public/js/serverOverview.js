$(document).ready(function(){
               $.ajax({
                   type: 'GET',
                   url: '/rest/api/hz/serverOverview',
                   timeout: 1000,
                   success: function(data) {
                     var cols = Object.keys(data.data[0])
                     for (var j = 0; j < cols.length; j++){
                        $('#tbody').append('<tr id="body">')
                        for (var i = 0; i < data.data.length; i++) {
                            var value = data.data[i][cols[j]]
                             $('#tbody').append('<td>'+value+'</td>')
                        }
                        $('#tbody').append('</tr>')
                     }
                   },
                   error: function (XMLHttpRequest, textStatus, errorThrown) {
                     console.log("Failed to send" + XMLHttpRequest)
                   }
               });
           });