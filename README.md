# DailyMartApplication
Luồng của dự án,
- Đăng nhập
  + Người dùng cần dùng đăng kí tài khoản
  + Sau đó đăng nhập, đăng nhập đúng sẽ vào trang chính, sai thì sẽ vẫn ở login nhưng chưa có tính năng báo lỗi đăng nhập sai
- Tìm sản phẩm
  + Người dùng cần nhập tên sản phẩm và bấm tìm kiếm
  + Vì dùng local database, nên nếu chưa có sản phẩm thì có thể vào My Store và thêm sản phẩm
- Thêm sản phẩm
  + Người dùng có thể vào My Store để tạo sản phẩm và nó sẽ chỉ hiện sản phẩm do chính người dùng tạo ở trong My Store
- Bộ Lọc
  +Người dùng sẽ check vào ô vuông của loại hàng mình cần tìm sau đó bấm apply filters, các sản phẩm của loại hàng đó sẽ hiện ra, có thể check nhiều loại hàng cùng một lúc
- Thêm giỏ hàng
  + Người dùng có thể thêm giỏ hàng từ ngoài màn hình chính nhưng số lượng mặc định là một
  + Người dùng có thể nhấp vào tên sản phẩm để vào trang chi tiết sản phẩm, tại đây người dùng có thể chỉnh số lượng và thêm vào giỏ hàng
  + Số bên cạnh giỏ hàng sẽ tăng lên theo số sản phẩm trong giỏ hàng
- Bỏ sản phẩm ra khỏi giỏ hàng
  + Người dùng có thể bấm Remove From Cart để bỏ ra khỏi giỏ hàng
  + Khi thêm sản phẩm vào giỏ hàng thì nút sẽ chuyển sang nút Remove From Cart
  + Người dùng có thể bấm nút này ở trang chính, trang chi tiết sản phẩm, và trong trang giỏ hàng
- Thanh Toán
  + Khi vào giỏ hàng người dùng sẽ nhập địa chỉ và ghi chú hoặc bỏ bớt sản phẩm ra khỏi giỏ hàng sau đó bấm checkout để đặt hàng
  + Nút điều chỉnh số lượng của sản phẩm trong giỏ hàng vẫn chưa hoạt động, chưa phát triển tính năng đó
  + Sau khi bấm checkout thì giỏ hàng sẽ trống, và có thể xem Đơn đặt ở trong trang Profile
- Trang Profile
  + Người dùng có thể xem thông tin người dùng và danh sách đơn hàng
  + Khi bấm vào xem chi tiết đơn nó sẽ popup lên danh sách sản phẩm và số lượng
- Nhận Hàng
  + Khi vừa đặt hàng trạng thái đơn sẽ là PENDING
  + Khi đã nhận chúng ta sẽ bấm vào xem chi tiết đơn và bấm Confirm Receipt để xác nhận đã nhận hàng
  + Sau đó trạng thái đơn sẽ chuyển sang RECEIVED
- Log Out
  + Người dùng sẽ bấm Logout trên thanh điều hướng để logout. 
