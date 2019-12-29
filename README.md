# Parser  

JSON stands for JavaScript Object Notation. It is an independent data exchange format and is the best alternative for XML.  

Android provides four different classes to manipulate JSON data.  
These classes are `JSONArray`,`JSONObject`,`JSONStringer` and `JSONTokenizer`.    
The first step is to identify the fields in the JSON data in which you are interested in.  
For example in the JSON given below we interested in getting temperature only.  
```json
{
   "sys":
   {
      "country":"GB",
      "sunrise":1381107633,
      "sunset":1381149604
   },
   "weather":[
      {
         "id":711,
         "main":"Smoke",
         "description":"smoke",
         "icon":"50n"
      }
   ],
	
  "main":
   {
      "temp":304.15,
      "pressure":1009,
   }
}
```

| Method  | Description |
| ------------- | ------------- |
|Array `[]`| In a JSON file , square bracket `[` represents a JSON array |
|Objects `{}`| In a JSON file, curly bracket `{` represents a JSON object |
|Key| A JSON object contains a key that is just a string. Pairs of `key/value` make up a JSON object |
|Value| Each key has a value that could be string , integer or double e.t.c | 	


**JSON Parsing**  

For parsing a JSON object, we will create an object of class `JSONObject` and specify a string containing JSON data to it.  
```java
String in;
JSONObject reader = new JSONObject(in);
```
The last step is to parse the JSON. A JSON file consist of different object with different `key/value` pair e.t.c.  
So JSONObject has a separate function for parsing each of the component of JSON file.  

```java
JSONObject sys  = reader.getJSONObject("sys");
country = sys.getString("country");
JSONObject main  = reader.getJSONObject("main");
temperature = main.getString("temp");
```




