

fetch('https://cat-fact.herokuapp.com/facts/random')
    .then(response => response.json())
    .then(data => console.log(data.text))
    .catch(error => console.error(error));

const getRandomUser = () => {
    return new Promise((resolve, reject) => {
        fetch('https://randomuser.me/api/')
            .then(response => response.json())
            .then(data => resolve(data))
            .catch(error => reject(error));
    });
};

getRandomUser()
    .then(data => console.log(data))
    .catch(error => console.error(error));

