from flask import Flask, request, jsonify
import joblib

app = Flask(__name__)

model = joblib.load('model.pkl')

@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.json
        
        prediction = model.predict(data)
        
        return jsonify({'prediction': prediction.tolist()}), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 400

if __name__ == '__main__':
    app.run(debug=True)


#docker build -t my-flask-api .
#docker run -d -p 5000:5000 my-flask-api
