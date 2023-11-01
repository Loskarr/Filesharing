# Filesharing
Các function đã có của peer
1. Register all file: register file với fname = lname
2. Search a file: tìm kiếm file theo fname, chọn source download
3. Register a file: register file cụ thể bằng cách nhập lname, sau đó chọn fname
4. exit: ngừng client

Các function đã có của server
discover: sau khi enter dòng lệnh sẽ yêu cầu nhập tên của peer (peer-4 là peer4, peer-3 là peer3), đầu ra dưới dạng lname as fname

![image](https://github.com/Loskarr/Filesharing/assets/143611763/307b5f56-a72c-4a01-a464-565fdbc4e839)

ping: sau khi enter dòng lệnh sẽ yêu cầu nhập tên của peer. (chỉ có thể ping peer nếu peer đã ít nhất 1 lần register file), ở phía client nhận được thông báo khi server ping

![image](https://github.com/Loskarr/Filesharing/assets/143611763/d9647f26-9f6a-4031-b1aa-4323859a862e)

![image](https://github.com/Loskarr/Filesharing/assets/143611763/c18ef5e7-1934-48cb-978f-727132aa759a)

vì giao tiếp ping thực hiện ở 1 port nên tôi đã disable ping của peer3 để có thể test ping command trên cùng 1 máy 
nếu chạy peer3 khác máy với peer4 thì có thể enable ping của peer3 để có thể test, chỉ cần xóa dấu comment trong phần main của Peer.java

![image](https://github.com/Loskarr/Filesharing/assets/143611763/c218e4b9-aa32-4bfa-b1ab-452f1ab815db)


quit: ngừng server

![image](https://github.com/Loskarr/Filesharing/assets/143611763/2214a70d-d5d8-4fed-a5d6-1ec14f79a10d)

Một số error control đã thực hiện

phát hiện command không tồn tại

![image](https://github.com/Loskarr/Filesharing/assets/143611763/c4e02ff8-20f9-498d-b413-77ee84aeea12)

phát hiện file đã bị xóa
phía client 

![image](https://github.com/Loskarr/Filesharing/assets/143611763/30e79599-dceb-4882-b05f-80a3011ae4f8)
phía server 

![image](https://github.com/Loskarr/Filesharing/assets/143611763/d0d22c21-c6a3-4504-9570-1553f750f370)


Trước khi chạy phải chỉnh Ip để phù hợp với client trong phần initialize ở procedure.java của các client
![image](https://github.com/Loskarr/Filesharing/assets/143611763/a11ed9e1-0b92-499d-973b-5b9f4dafcd21)

Dùng ipconfig để biết được Ip address của máy mình
