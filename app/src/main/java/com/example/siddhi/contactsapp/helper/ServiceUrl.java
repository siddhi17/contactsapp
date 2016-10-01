package com.example.siddhi.contactsapp.helper;

/**
 * Created by Yogendra singh on 23-08-2016.
 */
public class ServiceUrl
    {
        public static String getBaseUrl()
        {       /* String server_url="http://104.131.162.126/contactsapi/";*/
               String server_url="http://xesoftwares.co.in/contactsapi/";
               return server_url;
        }
        public static String getregister()
        {
                String register_url="registerUser.php";
                return  register_url;
        }
        public static String getLoginUrl()
        {
                String login_url="loginUser.php";
                return  login_url;
        }
        public static String getContactsUrl()
        {
            String profile_url="getContacts.php";
            return  profile_url;
        }
        public static String getUser()
        {
            String User_url="getUser.php";
            return  User_url;
        }
        public static String getUpdateUserUrl()
        {
            String UpdateUser_url="updateUser.php";
            return  UpdateUser_url;
        }
        public static String getImageUserUrl()
        {
            String UpdateUser_url="profile_images/";
            return  UpdateUser_url;
        }
    }
