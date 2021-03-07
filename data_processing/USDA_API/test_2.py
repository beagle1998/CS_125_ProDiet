import requests
import json
import os.path
import random
import re

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

#https://api.nal.usda.gov/fdc/v1/food/380864?api_key=4tyWHiifHqlNL1cduKjveOldaJFVGI4I6oi57fP8

#
#USDA API KEY


#ans=call_API()
#print(ans)
def extract_json_ID(ID):
    s_Unit={}
    type_f=1
    v_1=True

    f_dict1=call_API_ID(str(ID), apiKey)
    f_Name=(f_dict1["description"]).lower()
    print("ID")

    with open(__location__+"\ID_test\pop_"+str(f_Name)+".json",'w') as outfile:
       json.dump(f_dict1,outfile,indent=4) #converts dict to json and writes as file
    #print(json.dumps(f_dict1,indent=4))

    if "labelNutrients" in f_dict1.keys():
        nut="labelNutrients"
       
    else:
        nut="foodNutrients"  
        type_f=2

    for i in n_vegan:
        if i in f_Name:
            v_1= False
    if type_f==1:
        if any(word in f_dict1["ingredients"].lower() for word in n_vegan):
            v_1=False
    else:
        if "inputFoods" in f_dict1.keys():
            if f_dict1["inputFoods"]!=[]:
                if "foodDescription" in f_dict1["inputFoods"][0]:
                    if any(word in (f_dict1["inputFoods"][0]["foodDescription"]).lower() for word in n_vegan):
                        v_1=False
                else:
                    if any(word in (f_dict1["inputFoods"][0]["ingredientDescription"]).lower() for word in n_vegan):
                        v_1=False
               

    return [f_Name,v_1]


    
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
    
    if l_dict["foods"]==[]:
        black_list.append(name)
        return 1
    else:
        l_dict2=l_dict["foods"][0]     #foods/l_dict2 is a list, get 1st one
        ID=l_dict2["fdcId"]    
    print(ID)
    return extract_json_ID(ID)




def usda_range(r1):#r1=how many numbers from 400000>
    final_dict={"Recipes" : {"Nonvegan":{},"Vegan":{}}}
    count=0
    vegan_dict=[]
    nonvegan_dict=[]
    for i in range(400000,400000+r1):  #break,lunch, dinner
        print(count,i)
        count+=1
        temp1=extract_json_ID(i)
        if temp1[1]==True:
            vegan_dict.append((temp1,i))
        else:
            nonvegan_dict.append((temp1,i))
    final_dict["Recipes"]["Nonvegan"].update({i:nonvegan_dict})
    final_dict["Recipes"]["Vegan"].update({i:vegan_dict})

    return final_dict
    


def usda_name(list1):#list of list of foodnames, ex [breakfast,lunch,dinnner]
    final_dict={"Recipes" : {"Nonvegan":{},"Vegan":{}}}
    count=0
    for i,e in list1.items():  #break,lunch, dinner
        vegan_dict=[]
        nonvegan_dict=[]
        for k in e: #toast,jam, etc
            print(count,k)
            count+=1
            temp1=extract_json_NAME(k)
            if temp1!=1:
                if temp1[1]==True:
                    vegan_dict.append((temp1,k))
                else:
                    nonvegan_dict.append((temp1,k))
        final_dict["Recipes"]["Nonvegan"].update({i:nonvegan_dict})
        final_dict["Recipes"]["Vegan"].update({i:vegan_dict})

    return final_dict

    
#main
#list1 = ("hotdog","burger", "spaghetti","meatball", "ham sandwich")




#list1=("hotdog", "spaghetti","corndog","salad","salmon","meatball", "ham sandwich", "granola","legumes", "pea soup","chicken noodle soup", "fried chicken", "fried rice")

#final_list={"Breakfast":b_list,"Lunch":l_list,"Dinner":d_list} v_list_1


with open(__location__+"\_food_ld_v.txt",'r') as f:
    v_list_3=f.readlines()
v_list_3=[x.strip() for x in v_list_3]
v_list_3=[x for x in v_list_3 if x]
print(v_list_3)


final_list={"vv":v_list_3}


j_dict=usda_name(final_list)


print(j_dict)
#j_dict=usda_range(100)

with open(__location__+"\_ld_v_check1.json",'w') as outfile:
       json.dump(j_dict,outfile,indent=4) #converts dict to json and writes as file

print(black_list)
#test_cheddar()
#test_simple()




#making some pseudo basic jsons


#dict1={} 















