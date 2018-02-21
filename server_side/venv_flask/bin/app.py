from flask import Flask, request, jsonify

app = Flask(__name__)

#@app.route('/todo/api/v1.0/tasks', methods=['GET'])
#def get_tasks():
#    return jsonify({'tasks': tasks})

@app.route('/hello', methods=['GET'])
def get_tasks():
    return 'Hello from Flask!'


@app.route('/sqrt', methods=['POST'])
def create_task():
#    if not request.json or not 'title' in request.json:
#        abort(400)

    data = request.get_data()
    data = int(data)
    return str(data*data)


if __name__ == '__main__':
    app.run(host= '0.0.0.0')
