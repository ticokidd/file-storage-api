**File Storage API**
----

#### Summary:

This project is a simple implementation of an API that allows creation, listing,
retrieval, deletion, and updates of any file. Updates do not modify existing
files, but instead create new versions of the same file. Files get saved to a
MySQL database. Basic unit testing and integration testing is provided.

#### To run the project

1. Make sure you have docker-compose installed on your machine
2. Clone the repository
3. Run `./mvnw install` to generate the JAR file (`./mvnw.cmd install` on Windows)
4. Run `docker-compose up --build -d`
5. The application will be running at `localhost:8080`

##### Notes / Points for future improvement:

- The endpoints 'Add New File and 'Add New File Version' could be coalesced into
a single POST endpoint that adds new files, and increases the version number when
a matching filename already exists; they exist separately simply to showcase
both POST and PUT usage,
- add proper exception handling instead of letting Spring return 400 and 500
errors,
- add logging,
- extend testing coverage

### Endpoints:

#### **Add New File**
  Stores the first version of a file.
  
* URL: `/files`

* Method: `POST`

* Data Params

  Required: (as form data) `file=[file-to-upload]`
   
* Success Response:
  
  * Code: `200 OK`
  
    Content: `{"name":"file-name.ext","version":1,"contentType":"content/type"}`
 
* Error Response:

  * Code: `400 BAD REQUEST`
  
    Possible causes:
    
        - Missing file or 'file' key in the form data
        - Tried to create a new file but a file with the same name already exists
          (see 'Add New File Version' endpoint)
  
  * Code: `500 INTERNAL SERVER ERROR`
    
      Possible causes:
      
          - File size exeeded allowed maximum of 16,380 KB

#### **Add New File Version**
  Stores a new version of an existing file.
  
* URL: `/files`

* Method: `PUT`

* Data Params

  Required: (as form data) `file=[file-to-upload]`
   
* Success Response:
  
  * Code: `200 OK`
  
    Content: `{"name":"file-name.ext","version":2,"contentType":"content/type"}`
 
* Error Response:

  * Code: `400 BAD REQUEST`
  
    Possible causes:
    
        - Missing file or 'file' key in the form data
        - Tried to create a new file version but no file with the same name exist
          (see 'Add New File' endpoint)

#### **Get File**
  Retrieves a file version. `version` query parameter is optional, without it
  the latest version will be retrieved.
  
* URL: `/files`

* Method: `GET`

* URL Params

   Required: `filename=[String]`
   
   Optional: `version=[Long]`
   
* Success Response:
  
  * Code: `200 OK`
  
    Content: `file to download`
 
* Error Response:

  * Code: `404 NOT FOUND`
  
    Possible causes:
    
        - No file exists with the given filename or version does not exist for 
          that filename.
          
  * Code: `400 BAD REQUEST`
  
    Possible causes:
    
        - No 'filename' query parameter was provided

#### **List Files**
  Retrieves a list of file information objects for all existing file versions.
  When `filename` query parameter is provided, The endpoint returns a list of
  file information objects for all files with a matching name. The list will be
  ordered by filename (asc) and version (desc).
  
* URL: `/files/list`

* Method: `GET`

* URL Params
   
   Optional: `filename=[String]`

* Related Entity:
  
  FileInfo = `{"name" : [String], "version" : [Number], "contentType" : [String]}`
   
* Success Response:
  
  * Code: `200 OK`
  
    Content: `[{"name":"file1.mp3","version":2,"contentType":"audio/mpeg"},
    {"name":"file1.mp3","version":1,"contentType":"audio/mpeg"},
    {"name":"file2.jpg","version":3,"contentType":"image/jpeg"},
    {"name":"file2.jpg","version":2,"contentType":"image/jpeg"},
    {"name":"file2.jpg","version":1,"contentType":"image/jpeg"},
    {"name":"file3.txt","version":1,"contentType":"plain/text"}]`
 
* Error Response:

  * Code: `404 NOT FOUND`
  
    Possible causes:
    
        - No file exists with the given filename or no files have been added yet.

#### **Delete File**
  Deletes a file version. `version` query parameter is optional, without it
  all versions of that file will be deleted.
  
* URL: `/files`

* Method: `GET`

* URL Params

   Required: `filename=[String]`
   
   Optional: `version=[Long]`
   
* Success Response:
  
  * Code: `204 NO CONTENT`

* Error Response:

  * Code: `400 BAD REQUEST`
  
    Possible causes:
    
        - No 'filename' query parameter was provided