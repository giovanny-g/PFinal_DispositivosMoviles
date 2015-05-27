package com.example.giovanny.fuelapp;

public class Vehicle {

    //private variables
    int _id;
    String _name;
    String _tank;

    public Vehicle(){

    }
    // constructor
    public Vehicle(int id, String name, String tank){
        this._id = id;
        this._name = name;
        this._tank = tank;
    }

    // constructor
    public Vehicle(String name, String tank){

        this._name = name;
        this._tank = tank;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }
    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    // getting phone number
    public String getTank(){
        return this._tank;
    }

    // setting phone number
    public void setTank(String phone_number){
        this._tank = phone_number;
    }
}