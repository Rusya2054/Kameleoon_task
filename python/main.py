import requests
from fastapi import FastAPI, HTTPException

app = FastAPI()


@app.get("/geocode/{city_name}")
async def get_coordinates(city_name: str):
    url = f'https://nominatim.openstreetmap.org/search'
    params = {
        'q': city_name,
        'format': 'json',
        'limit': 1
    }

    headers = {
        'User-Agent': 'MyGeoApp/1.0'
    }
    response = requests.get(url, params=params, headers=headers)
    print(response)
    if response.status_code != 200:
        raise HTTPException(status_code=500, detail="Error with geocoding service.")

    data = response.json()
    if data:
        latitude = data[0]['lat']
        longitude = data[0]['lon']
        return {"city": city_name, "latitude": latitude, "longitude": longitude}
    else:
        raise HTTPException(status_code=404, detail="City not found")

