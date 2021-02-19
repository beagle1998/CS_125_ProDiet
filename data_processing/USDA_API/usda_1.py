import requests
import json
import os.path
import random

__location__ = os.path.realpath(
    os.path.join(os.getcwd(), os.path.dirname(__file__)))

apiKey="4tyWHiifHqlNL1cduKjveOldaJFVGI4I6oi57fP8"

n_vegan={"pork","beef", "chicken", "turkey", "lamb", "goat", "meat", "sausage", "seafood","fish","cod","tuna","salmon", "egg", "poutry", "shrimp", "lactose", "milk", "egg", "butter", "honey", "cream"}

def call_API_foodName(foodName, apiKey):
    url = f'https://api.nal.usda.gov/fdc/v1/foods/search?api_key={apiKey}&query={foodName}'
    r = requests.get(url)
    #print(r.status_code)  # 200
    return r.json()


def call_API_ID(foodID, apiKey):#returns json as dict
    url = f'https://api.nal.usda.gov/fdc/v1/food/{foodID}?api_key={apiKey}'
    r = requests.get(url)
    #print(r.status_code)  # 200
   
    return r.json()

def call_API_4(foodID, apiKey):#returns json POST REQEST
    url = f'https://api.nal.usda.gov/fdc/v1/food/{foodID}?api_key={apiKey}'
    r = requests.post(url)
    print(r.status_code)  # 200
    print(type(r))
    print(type(r.json()))
    return r.json()

def call_API_5(foodID, apiKey):#returns json POST REQEST
    params = {'api_key': apiKey}
    data = {'generalSearchInput': 'chia'}
    response = requests.post(
        r'https://api.nal.usda.gov/fdc/v1/search',
        params=params,
        json=data
    )
    return (response.json())

#https://api.nal.usda.gov/fdc/v1/food/380864?api_key=DEMO_KEY
#ans = call_API_2("Cheddar cheese", "DEMO_KEY")


#4tyWHiifHqlNL1cduKjveOldaJFVGI4I6oi57fP8
#USDA API KEY


#ans=call_API()
#print(ans)
def extract_json_ID(ID):
    
    f_dict1=call_API_ID(str(ID), apiKey)
    f_Name=(f_dict1["description"]).lower()

    with open(__location__+"\ID_test\pop_"+str(f_Name)+".json",'w') as outfile:
       json.dump(f_dict1,outfile,indent=4) #converts dict to json and writes as file
    #print(json.dumps(f_dict1,indent=4))

    n_dict={"fdcID": ID, "calories":0, "carbohydrates":0, "fat":0, "protein":0, "sugars" :0}#nutrients we want to know about dict
    
    s_Size={"servingSize" : f_dict1["servingSize"]}
    s_Unit=f_dict1["servingSizeUnit"]
    if s_Unit!="g":
        print((ID,f_Name,s_Unit))
    if s_Unit.lower()=="kg":
        s_Size=s_Size*1000

    if "labelNutrients" in f_dict1.keys():
        nut="labelNutrients"
    else:
        nut="foodNutrients"  

    #getting nutrient details we want
    for n,v in n_dict.items():
        if n in f_dict1[nut]:
            n_dict[n]=f_dict1[nut][n]["value"]

    n_dict["vegan"]="yes"
    if any(word in f_dict1["ingredients"].lower() for word in n_vegan):
        n_dict["vegan"]="no"
    
        

    s_Size.update(n_dict)
    return_dict={}
    return_dict[f_Name]=s_Size

    return return_dict
    
def check_Units(r1):#insert number for how many fcids to check? do random next?
    w_dict={}
    for n in range(400000,400000+r1):
        f_dict1=call_API_ID("400000", apiKey)
        unit=f_dict1["servingSizeUnit"]
        if unit!="g":
            w_dict[n]=unit
    print(w_dict)
    

#testing food id cheddaar cheese specific 380864

    
def extract_json_NAME(name):#enter food name    #no serving unit? is it score?
    return_dict={}
    l_dict=call_API_foodName(name, apiKey) #foodnames, hold the nutrients, but not the label nutrients
    l_dict2=l_dict["foods"][0]     #foods/l_dict2 is a list, get 1st one
    ID=l_dict2["fdcId"]    
    return extract_json_ID(ID)




def usda_range(r1):#r1=how many numbers from 400000>
    
    import json
    final_dict={"Recipes" : {}}
    w_dict={}
    for n in range(400000,400000+r1):
        final_dict["Recipes"].update(extract_json_ID(n))

    return final_dict
    


def usda_name(list1):#list of food names, of which will find lists of foods of the name, from which will get the information for the first one? or all?
    final_dict={"Recipes" : {}}
    for i in list1:
        final_dict["Recipes"].update(extract_json_NAME(i))
    return final_dict

    
#main
#list1 = ("hotdog","burger", "spaghetti","meatball", "ham sandwich")
list1=("hotdog", "spaghetti","corndog","salad","salmon","meatball", "ham sandwich", "granola","legumes", "pea soup","chicken noodle soup", "fried chicken", "fried rice")
j_dict=usda_name(list1)

#j_dict=usda_range(100)

with open(__location__+"\_100_ID_2.json",'w') as outfile:
       json.dump(j_dict,outfile,indent=4) #converts dict to json and writes as file
#test_cheddar()
#test_simple()




#making some pseudo basic jsons


#dict1={} 















