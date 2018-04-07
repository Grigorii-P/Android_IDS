from flask import Flask, request, jsonify
import hashlib

app = Flask(__name__)

#@app.route('/todo/api/v1.0/tasks', methods=['GET'])
#def get_tasks():
#    return jsonify({'tasks': tasks})

secret = 12378987123
N = '{0:01024b}'.format(secret)
database = {}


def add_to_database(data):
    usr = data[0] + N
    psw = hashlib.sha256(bytes(data[0] + N + data[1], encoding='utf-8')).hexdigest()
    database[usr] = psw


@app.route('/put_data', methods=['POST'])
def add_user():
    try:
        data = str(request.values)
        data = data.split('_')
        add_to_database(data)
        print('size of database is ', len(database))
        return 'good request'
    except:
        return 'bad request'



if __name__ == '__main__':
    app.run(host= '0.0.0.0')
