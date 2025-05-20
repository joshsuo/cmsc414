import requests # pip3 install requests may be necessary (windows or macOS)
import sys

'''
Session cookie should be in sys.argv[1]
Argument should be session cookie itself,
not `session=...`

If you are unsure what part of the string
is the session cookie, use the inspector while 
visiting now.share and look for cookies in storage
'''


if len(sys.argv) < 2:
	print("Usage: python3 task5.py <session-cookie>")
	sys.exit(1)

session_cookie = sys.argv[1]
print("\nsession cookie is:", session_cookie, "\n")


# Update as needed
url = " http://localhost/update_profile"

# Complete
headers = {
	'Origin': 'http://localhost',
	'User-agent':'CMSC414-Forge',
    #'Content-Type':'application/x-www-form-urlencoded',
    #'Referrer Policy':'strict-origin-when-cross-origin',
    'Cookie':'session=' + session_cookie
}



# Complete
data = {
	'full_name':'Bob Reed',
    'description':'Alice is my’s best friend <3'
}

r = requests.post(url, timeout=(6, 9), data=data, headers=headers)
print(r)
print("\n and here is the response content:\n")
print(r.content)