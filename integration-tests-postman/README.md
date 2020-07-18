**How to use this for Postman testing:**

**Setting up:**
1) Download File
2) Open Postman, click import, select the file
3) Once file appears, ensure {{baseUrl}} is set up to the correct server (default localhost:9091). This can be found by right clicking the collection > Edit > Variables > Initial + Current value
4) Set up the token by obtaining it from portal API post > Right click testing collection > Edit > Authorization > Paste token in "Access Token" box and click update.

**Running Sanity Tests:**
1) Hover over collection and click the play arrow
2) Click run. This will open the "Collection Runner"
3) On Collection Runner, click "Run ..."
4) If everything works as it should, you should see 20 passed and 0 failed.

**Common Codes:**

200 - Successful request
403 - Authorization error (likely expired access token)
500- General API error, usually due to providing an invalid request (IE. Format error, putting a string in an int field)
