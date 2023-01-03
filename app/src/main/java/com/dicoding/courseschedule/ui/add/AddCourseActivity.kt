package com.dicoding.courseschedule.ui.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.home.HomeViewModel
import com.dicoding.courseschedule.ui.home.HomeViewModelFactory
import com.dicoding.courseschedule.ui.list.ListActivity
import com.dicoding.courseschedule.util.Event
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import java.lang.StringBuilder
import java.util.*

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var addCourseViewModel: AddCourseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        val factory = AddCourseViewModelFactory.createFactory(this)
        addCourseViewModel = ViewModelProvider(this, factory).get(AddCourseViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                addCourseViewModel.insertCourse(
                    findViewById<EditText>(R.id.add_ed_course_name).text.toString(),
                    findViewById<Spinner>(R.id.day_spinner).selectedItemPosition,
                    findViewById<TextView>(R.id.start_time_tv).text.toString(),
                    findViewById<TextView>(R.id.end_time_tv).text.toString(),
                    findViewById<EditText>(R.id.add_ed_lecturer).text.toString(),
                    findViewById<EditText>(R.id.add_ed_note).text.toString(),
                )
                addCourseViewModel.saved.observe(this){
                    if (it.getContentIfNotHandled() == true){
                        Toast.makeText(this, "Course added successfully!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, ListActivity::class.java))
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showTimePicker1(view : View){
        val dialogFragment =TimePickerFragment()
        dialogFragment.show(supportFragmentManager, "timePicker1")
    }
    fun showTimePicker2(view : View){
        val dialogFragment =TimePickerFragment()
        dialogFragment.show(supportFragmentManager, "timePicker2")
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int){
        val time = String.format("%02d:%02d", hour, minute)
        when(tag){
            "timePicker1" -> findViewById<TextView>(R.id.start_time_tv).text = time
            "timePicker2" -> findViewById<TextView>(R.id.end_time_tv).text = time
        }
    }
}