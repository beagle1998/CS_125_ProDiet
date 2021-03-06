import requests
import json
import os.path
import random

__location__ = os.path.realpath(
    os.path.join(os.getcwd(), os.path.dirname(__file__)))

apiKey="4tyWHiifHqlNL1cduKjveOldaJFVGI4I6oi57fP8"

n_vegan={"loin", "honey","duck","brisket","walleye","sushi","adobo","omelet","pepperoni","lamb","veal","halibut","macherel","bacon","rib","cheese", "butter", "oysters","pork","beef", "chicken", "turkey", "lamb", "goat", "meat", "sausage","mollusk", "seafood","squid","octopus","fish","cod","tuna","salmon", "egg", "poutry","scallops","fish","fish" "crab", "lobster", "shrimp", "lactose", "milk", "egg", "butter", "honey", "cream", "yogurt", "yoghurt","steak","sirloin"}

black_list=[]

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
    s_Unit={}
    type_f=1

    f_dict1=call_API_ID(str(ID), apiKey)
    f_Name=(f_dict1["description"]).lower()

    with open(__location__+"\ID_test\pop_"+str(f_Name)+".json",'w') as outfile:
       json.dump(f_dict1,outfile,indent=4) #converts dict to json and writes as file
    #print(json.dumps(f_dict1,indent=4))

    n_dict={"foodName":f_Name, "fdcID": ID, "calories":0, "carbohydrates":0, "fat":0, "protein":0}#nutrients we want to know about dict
    n3_dict={"energy":"calories", "carbohydrate, by difference":"carbohydrates", "total lipid (fat)":"fat", "protein":"protein"}

    if "labelNutrients" in f_dict1.keys() and f_dict1["labelNutrients"]!={}:
        nut="labelNutrients"
        if "servingSize" in f_dict1.keys():
            s_Size={"servingSize" : f_dict1["servingSize"]}
        else:
            s_Size={"servingSize": 250}#idk about this?>
        s_Unit=f_dict1["servingSizeUnit"]
        if s_Unit!="g":
            print((ID,f_Name,s_Unit))
        if s_Unit.lower()=="kg":
            s_Size=s_Size*1000

    else:
        nut="foodNutrients"  
        type_f=2
        if f_dict1["foodPortions"]!=[]:
            s_Size={"servingSize" : f_dict1["foodPortions"][0]["gramWeight"]}
        else:
            s_Size={"servingSize" : 250}
    

    #getting nutrient details we want
    if type_f==1:
        for n,v in n_dict.items():
            if n in f_dict1[nut]:
                n_dict[n]=f_dict1[nut][n]["value"]
    else:
        for n,v in n3_dict.items():
            for i in f_dict1[nut]:
                if "amount" in i.keys():
                    if n==(i["nutrient"]["name"].lower()):
                        n_dict[v]=i["amount"]

    
    n_dict["vegan"]=True
    for i in n_vegan:
        if i in f_Name:
            n_dict["vegan"]=False
    if type_f==1:
        if any(word in f_dict1["ingredients"].lower() for word in n_vegan):
            n_dict["vegan"]=False
    else:
          if "inputFoods" in f_dict1.keys():
            if f_dict1["inputFoods"]!=[]:
                if "foodDescription" in f_dict1["inputFoods"][0]:
                    if any(word in (f_dict1["inputFoods"][0]["foodDescription"]).lower() for word in n_vegan):
                        v_1=False
                else:
                    if any(word in (f_dict1["inputFoods"][0]["ingredientDescription"]).lower() for word in n_vegan):
                        v_1=False
            


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
    #print(name)
    if l_dict["foods"]==[]:
        black_list.append(name)
        return 1
    else:
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
    


def usda_name(list1):#list of list of foodnames, ex [breakfast,lunch,dinnner]
    final_dict={"Recipes" : {"Nonvegan":{},"Vegan":{}}}
    count=0
    for i,e in list1.items():  #break,lunch, dinner
        vegan_dict={}
        nonvegan_dict={}
        for k in e: #toast,jam, etc
            print(count,k)
            count+=1
            temp1=(extract_json_NAME(k))
            for j,v in temp1.items():
                if v.pop('vegan')==True:
                    vegan_dict.update(temp1)
                else:
                    nonvegan_dict.update(temp1)


        final_dict["Recipes"]["Nonvegan"].update({i:nonvegan_dict})
        final_dict["Recipes"]["Vegan"].update({i:vegan_dict})


    return final_dict

    
#main


#list1=("hotdog", "spaghetti","corndog","salad","salmon","meatball", "ham sandwich", "granola","legumes", "pea soup","chicken noodle soup", "fried chicken", "fried rice")
'''
with open(__location__+"\_food_b.txt",'r') as f:
    b_list=f.readlines()
b_list=[x.strip() for x in b_list]
b_list=[x for x in b_list if x]


with open(__location__+"\_blackened.txt",'r') as f:
    l_list=f.readlines()
l_list=[x.strip() for x in l_list]
l_list=[x for x in l_list if x]
'''
with open(__location__+"\_food_d.txt",'r') as f:
    d_list=f.readlines()
d_list=[x.strip() for x in d_list]
d_list=[x for x in d_list if x]

#"Breakfast":b_list
#"Lunch":l_list
final_list={"Dinner":d_list}


j_dict=usda_name(final_list)


with open(__location__+"\_30_dinner.json",'w') as outfile:
       json.dump(j_dict,outfile,indent=4) #converts dict to json and writes as file

print(black_list)



#making some pseudo basic jsons


#dict1={} 















