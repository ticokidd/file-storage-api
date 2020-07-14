**File Storage API**
----

#### Summary:

This project is a simple implementation of an API that allows creation, listing,
retrieval, deletion, and updates of any file. Updates do not modify existing
files, but instead create new versions of the same file. Files get saved to a
MySQL database. Basic unit testing and integration testing is provided.

##### Notes:

The endpoints 'Add New File and 'Add New File Version' could be coalesced into a
single POST endpoint that adds new files, and increases the version number when
a matching filename already exists; they are separated simply to showcase both
POST and PUT usage.

### Endpoints:

#### **Add New File**
  Stores the first version of a file.
  
* URL: `/file/add_new`

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

#### **Add New File Version**
  Stores a new version of an existing file.
  
* URL: `/file/add_new_version`

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
  
* URL: `/file`

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
  
* URL: `/file/list`

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
  
* URL: `/file`

* Method: `GET`

* URL Params

   Required: `filename=[String]`
   
   Optional: `version=[Long]`
   
* Success Response:
  
  * Code: `204 NO CONTENT`

  * Code: `400 BAD REQUEST`
  
    Possible causes:
    
        - No 'filename' query parameter was provided