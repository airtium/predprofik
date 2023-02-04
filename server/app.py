from flask import Flask, request
import psycopg2

app = Flask(__name__)

@app.route("/", methods=["GET"])
def hello():
    return "Hello World!"

@app.route('/product/add', methods=['GET', 'POST'])
def productAdd():
    try:
        conn = psycopg2.connect(database="predprofik",
                                host="predprofik.ciywqchtdyrs.eu-west-1.rds.amazonaws.com",
                                user="predprofik",
                                password="pKEgUOx3BzUkvC2MpD4e",
                                port="5432")

    except:
        print("Something is wrong")
        return 500
    product = request.args.get('product')
    cursor = conn.cursor()
    query = "INSERT INTO products (id_user, id_list, id_status, name) VALUES (1, 1, 1, '"+product+"')"
    cursor.execute(query)
    conn.commit()
    conn.close()
    return "Products are added!"

@app.route('/product/listall', methods=['GET', 'POST'])
def productsList():
    # здесь мы обращаемся к базе данных и показываем список продуктов
    #try:
    conn = psycopg2.connect(database="predprofik",
                                host="predprofik.ciywqchtdyrs.eu-west-1.rds.amazonaws.com",
                                user="predprofik",
                                password="pKEgUOx3BzUkvC2MpD4e",
                                port="5432")

    #except:
        #print("Something is wrong")
        #return 500
    cursor = conn.cursor()
    query = ("SELECT id_product,name FROM products")
    cursor.execute(query)
    res = cursor.fetchall()
    a = []
    for (productID, productName) in res:
        a.append("Продукт номер "+str(productID)+", название: "+productName)
        print("Получили из базы продукт номер ", productID)
    conn.close()
    b=''
    for x in a:
        b=b+"<p>"+x+"</p>"
    return b

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=8000)