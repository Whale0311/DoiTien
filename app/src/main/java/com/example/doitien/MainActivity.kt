package vn.edu.listexamples

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
//Khai báo các biến trong giao diện
    private lateinit var etAmount: EditText //Số tiền cần chuyển đổi
    private lateinit var spinnerFrom: Spinner //Danh sách nguồn tiền chuyển đổi
    private lateinit var spinnerTo: Spinner //Danh sách loại tiền đích muốn đổi
    private lateinit var tvExchangeRate: TextView //Hiển thị tỷ giá
    private lateinit var etResult: EditText //Hiển thị giá trị sau khi đổi
//Khai báo tỉ giá bằng mapOf để tạo các cặp khóa và giá trị
    private val currencyRates = mapOf(
        "USD" to 1.0,
        "VND" to 23185.0,
        "JPY" to 150.0,
        "CNY" to 7.2,
        "EUR" to 0.91
    )
    //Khai báo kí tự hiển thị khi chuyển đổi
    private val currencySymbols = mapOf(
        "USD" to "$",
        "VND" to "₫",
        "JPY" to "¥",
        "CNY" to "¥",
        "EUR" to "€"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//Gán id trong file layout
        etAmount = findViewById(R.id.etAmount)
        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
        tvExchangeRate = findViewById(R.id.tvExchangeRate)
        etResult = findViewById(R.id.etResult)
//Khai báo danh sách tiền tệ bằng key của DS tỉ giá
// Khái báo adapter và dùng ArrayAdapter để hiển thị trong danh sách Spinner
        val currencyList = currencyRates.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, currencyList)
        spinnerFrom.adapter = adapter //Danh sách spinner của loại tiền nguồn
        spinnerTo.adapter = adapter //Danh sách spinner của loại tiền đích
//Xử lý sự kiện khi chọn loại tiền nguồn sẽ gọi hàm cập nhật tỉ giá và kết quả
        spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateConversion()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
//Tương tự khi chọn loại tiền đích
        spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateConversion()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
//Xử lý sự kiện khi nhập số tiền
        etAmount.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                updateConversion()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
//Hàm đổi tiền và cập nhật tỉ giá
    private fun updateConversion() {
        val fromCurrency = spinnerFrom.selectedItem.toString()
        val toCurrency = spinnerTo.selectedItem.toString()
        val amountText = etAmount.text.toString()

        if (amountText.isNotEmpty()) {
            val amount = amountText.toDoubleOrNull() ?: 0.0
            val rate = (currencyRates[toCurrency] ?: 1.0) / (currencyRates[fromCurrency] ?: 1.0)
            val convertedAmount = amount * rate

            tvExchangeRate.text = "Tỉ giá: 1 $fromCurrency = ${"%.2f".format(rate)} $toCurrency"
            val symbol = currencySymbols[toCurrency] ?: ""
            etResult.setText("$symbol ${"%.2f".format(convertedAmount)}")
        } else {
            tvExchangeRate.text = "Tỉ giá: --"
            etResult.setText("")
        }
    }
}

