import UserType from "../model/user"

let currentUser:UserType;

const setCurrentUser = (user:UserType) => {
    currentUser = user;
}

const getCurrentUser = ():UserType => {
    return currentUser;
}

export {
    setCurrentUser,
    getCurrentUser
}


