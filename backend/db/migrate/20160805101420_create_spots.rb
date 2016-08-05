class CreateSpots < ActiveRecord::Migration[5.0]
  def change
    create_table :spots do |t|
      t.references :mission, foreign_key: true

      t.string :name
      t.string :description
      t.string :picture

      t.float :latitude
      t.float :longitude

      t.timestamps
    end
  end
end
