package co.viniciuspinheiro.netflixremake.kotlin

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import co.viniciuspinheiro.netflixremake.MainActivity
import co.viniciuspinheiro.netflixremake.R
import co.viniciuspinheiro.netflixremake.model.Category
import co.viniciuspinheiro.netflixremake.util.CategoryTask

class MainActivity : AppCompatActivity() {
    //inicio
    override fun onCreate(savedInstanceState: Bundle?) {
        //declarando onCreate
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //declarando o mainAdapter na recyclerview
        val categories = arrayListOf<Category>()
        findViewById<RecyclerView>(R.id.recycler_view_main).adapter = MainAdapter(categories)
        //declarando o categoryTask e categoryLoader
        val categoryTask = CategoryTask(this)
        categoryTask.setCategoryLoader {
            it.size
        }
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home")
    }

    //criar um MainAdapter que espera uma lista de categorias
    private inner class MainAdapter(val categories: List<Category>) :
        RecyclerView.Adapter<CategoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
            return CategoryHolder(layoutInflater.inflate(R.layout.category_item, parent, false))
        }

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            val category = categories[position]
            holder.bind(category)
        }

        override fun getItemCount(): Int = categories.size
    }

    //criar lista de dados
    private class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category) {
            itemView.findViewById<TextView>(R.id.text_view_title).text = category.name
        }

    }
}