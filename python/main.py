# main.py
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import requests
import ipaddress
from datetime import datetime
import pytz

app = FastAPI()

class IPInfo(BaseModel):
    ip: str
    latitude: float
    longitude: float
    city: str
    current_time: str

class ErrorResponse(BaseModel):
    detail: str

def is_global_ip(ip: str) -> bool:
    try:
        addr = ipaddress.ip_address(ip)
        return addr.is_global and not (
            addr.is_private or
            addr.is_loopback or
            addr.is_link_local or
            addr.is_reserved
        )
    except ValueError:
        return False

def get_ip_info(ip: str) -> dict:
    try:
        response = requests.get(
            f"http://ip-api.com/json/{ip}?fields=status,message,country,city,lat,lon,timezone",
            timeout=5
        )
        data = response.json()

        if data['status'] == 'fail':
            raise ValueError(data['message'])

        return data
    except requests.exceptions.RequestException:
        raise RuntimeError("Could not connect to IP information service")

@app.get("/ip-info/{ip_address}",
         response_model=IPInfo,
         responses={
             400: {"model": ErrorResponse},
             500: {"model": ErrorResponse}
         })
async def get_ip_info_endpoint(ip_address: str):
    try:
        ip_obj = ipaddress.ip_address(ip_address)
    except ValueError:
        raise HTTPException(
            status_code=400,
            detail="Invalid IP address format"
        )

    if not is_global_ip(ip_address):
        raise HTTPException(
            status_code=400,
            detail="Private or reserved IP address range"
        )

    try:
        ip_data = get_ip_info(ip_address)
    except Exception as e:
        error_msg = str(e)
        if any(msg in error_msg.lower() for msg in ["reserved", "private", "invalid"]):
            status_code = 400
        else:
            status_code = 500
        raise HTTPException(
            status_code=status_code,
            detail=error_msg
        )

    try:
        timezone = pytz.timezone(ip_data['timezone'])
        current_time = datetime.now(timezone).strftime("%Y-%m-%d %H:%M:%S")
    except pytz.exceptions.UnknownTimeZoneError:
        current_time = "Unknown"

    return {
        "ip": ip_address,
        "latitude": ip_data['lat'],
        "longitude": ip_data['lon'],
        "city": ip_data['city'],
        "current_time": current_time
    }