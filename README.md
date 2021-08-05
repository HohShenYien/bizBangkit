# bizBangkit

## Initialize the workspace

1. open terminal / command prompt
2. move to the folder you want to store by cd

    e.g. The terminal will display this when you open it
    
    ```
    C:\Users\ACER>
    ```
    You want to store it in a folder called codes in Documents folder, you type
    
    ```
    C:\Users\ACER> cd Documents/codes
    ```
    
    The exact location can be found at the top of your file explorer, you click the top bar
    
    ![Example](https://uis.georgetown.edu/wp-content/uploads/2019/05/win10-fileexplorer-addrbar.png)
    
4. git clone this repository
    
     ```
    C:\Users\ACER\Documents\codes> git clone https://github.com/HohShenYien/bizBangkit
    ```
    
    Because this repository is a private repository, you need to type in your username and password to clone it.
    
6. start working :D


## Uploading your work

1. Move to the project folder in terminal / command prompt
2. git add everything
    
    ```
    C:\Users\ACER\Documents\codes\bizBangkit> git add .
    ```
    
3. commit and write your message (try to be clear on the message in case need to revert it...)
    
    ```
    C:\Users\ACER\Documents\codes\bizBangkit> git commit -m "Created homepage"
    ```
    
4. push it to github (storing it in github)
    
    ```
    C:\Users\ACER\Documents\codes\bizBangkit> git push
    ```
    
    You will have to enter the username and password again here (unless you use token in vs code or something)
    
- After pushing, you can see the changes you made in github.
- Sometimes when you push there might be two problems:
1. Not up to date

    This is because someone uploaded their work, and you didn't download those work
    
    - Do a git pull
    
        ```
        C:\Users\ACER\Documents\codes\bizBangkit> git pull
        ```
        
2. Conflicting

    This occurs when your change conflict with another person's change
    
    Maybe send a message to the group if this occurs?
    
    Generally:
    
    1. Create a new branch
    2. Create a new [pull request](https://docs.github.com/en/github/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request)
    3. Merge it
