# task 1 
using the docker exec(terminal), i found the username for Kathy as kathy
therefore the username is correct.
for the password, since the code is written as:
password = '" + {password} + "';
so taking advantage of single quotes, for the password i wrote:
' or '' = '
when inserted into the code, it will read:
'' or '' = ''
and since '' = '' is always true,
logging in as kathy will be successful

# task 2
the code it written as:
update users set password = '" + new_pw + "' ...
setting new_pw as:
h\'\'cked' where username = 'charlie' -- 
so the code will read:
update users set password = 
'h\'\'cked where username = 'charlie' -- 
and since we add the comment( -- ),
everything else after that statement can be "erased"
but the new_pw and the confirm_pw must be the same
since it is checked whether or not they are the same password
also the old_pw can be anything
since after the comment in new_pw
old_pw is not used and therefore not checked
whether or not it is the same as the old password

# task 3
evewilson%27%2C%20%27bob%27%29%20--%20 (encoded)
evewilson', 'bob') --  (decoded)
the code it written as:
... values ('" + follow + "', '"...);"
since the follow variable is what comes after modify_relation/
if i put the decoded follow variable in the code:
... values ('evewilson', bob') -- )...
and because of the comment, everything after it is void,
it takes in evewilson as the follow, and bob as the user,
so that when the code executes, 
it will cause bob to follow evewilson
also Eve's username was found through docker exec in the users database

# task 4
to steal bob's cookie info, we need to put some code on
a page that bob is looking at
to send bob's cookie info to the echo server
we use an image html tag on alice's profile
so it makes another request to the echo server
inside the image html tag the source attribute
write the link to echo server that includes the
username and the cookie info, that can be seen on echo server terminal in docker
so to get the cookie, simply write document.cookie to access the cookie
for the user, write document.getElementById("current_user").innerText
since there is an id for current_user that can show who the user is.
and putting that all into the link that is sent to the echo server,
the echo server therefore shows who the user is and what the cookie is

# task 5
since we have bob's cookie, and the task5.py file
takes in the session cookie as an argument 
so in headers, i added the session cookie of bob
to impersonate bob
and in the data, full_name and description was modified
(which are the variables required to update profile):
full_name: 'Bob Reed'
description: 'Alice is my’s best friend <3'
so that when the request is sent, it mimics the
act of updating bob's profile to the description

# task 6
make a simple html page with an image url
this url makes a request to nowshare, that modifies the relationship
between kathy and alice
so when the html file is opened, the image src link creates a get request 
since alice is already logged in as herself
the web browser automatically attaches alice's session cookie 
to this request and executes the modify_relation with kathy 



