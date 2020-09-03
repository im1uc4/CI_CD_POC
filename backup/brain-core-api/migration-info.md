SELECT * FROM optimisation.constraint_param_values;

node.js code 

db.constraint_param_structure.hasMany(db.constraint_param_value ,{foreignKey: 'constraint_param_key_id', sourceKey: 'id'});
db.constraint_param_value.belongsTo(db.constraint_param_structure,{foreignKey: 'constraint_param_key_id', sourceKey: 'id'});


The foreign key is constraint_param_key_id

In Java Code 


constraint_param_value has foreign key as constraint_param_structure_id



        if(insModelHeader.model_id>0){
            await deleteModel(insModelHeader.model_id)
          }