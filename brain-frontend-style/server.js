const fs = require('fs')
const jsonServer = require('json-server')
const bodyParser = require('body-parser')
const server = jsonServer.create()
const router = jsonServer.router('./api/db.json')
var jwt = require('jsonwebtoken');


const middlewares = jsonServer.defaults()
const userdb = JSON.parse(fs.readFileSync('./api/users.json', 'UTF-8'))
const constrdb = JSON.parse(fs.readFileSync('./api/constraints.json', 'UTF-8'))
const jobsdb = JSON.parse(fs.readFileSync('./api/jobs.json', 'UTF-8'))
const ds_skeletondb = JSON.parse(fs.readFileSync('./api/dataset_frame.json', 'UTF-8'))
const ds_list = JSON.parse(fs.readFileSync('./api/datasets_list.json', 'UTF-8'))
const ds_column_headers= JSON.parse(fs.readFileSync('./api/dataset_tables_skeleton.json', 'UTF-8'))
const ds_data= JSON.parse(fs.readFileSync('./api/dataset_data.json', 'UTF-8'))

// Set default middlewares (logger, static, cors and no-cache)
server.use(middlewares)
server.use(bodyParser.urlencoded({extended: true}))
server.use(bodyParser.json())

// Create custom functionms
const SECRET_KEY = "rapidlab-secret-key"

const expiresIn = '1h'

// Create a token from a payload 
function createToken(payload){
  return jwt.sign(payload, SECRET_KEY, {expiresIn})
}

// Verify the token 
function verifyToken(token){
  return  jwt.verify(token, SECRET_KEY, (err, decode) => decode !== undefined ?  decode : err)
}

// Check if the user exists in database
function isAuthenticated({username, password}){
  let usr= userdb.users.find(user=> user.username=== username && user.password=== password && user.status===true)
  
  return (usr !== -1 && usr!=undefined)
 
} 

server.get('/api/v1/getconstraints',(req,res)=>{
  const constraints=constrdb.constraints.filter(cntrs => cntrs.model_id === Number(req.headers.model_id) && cntrs.status===1)
  const status = 200 
  if (!constraints)
  {
      const reason = 'No data found.'
      res.status(status).json({ "result":reason})
      return
  }
  res.status(status).json({constraints})
  return
});

server.get('/api/v1/getjobsformodel',(req,res)=>{
  const jobs=jobsdb.jobs.filter(job => job.model_id === Number(req.headers.model_id))
  const status = 200 
  if (!jobs)
  {
      const reason = 'No data found.'
      res.status(status).json({ "result":reason})
      return
  }
  res.status(status).json({jobs})
  return
});

server.get('/api/v1/getdsformodel',(req,res)=>{
  const ds_lists=ds_list.ds.filter(ds => ds.model_id === Number(req.headers.model_id))
  const status = 200 
  if (!ds_lists)
  {
      const reason = 'No data found.'
      res.status(status).json({ "result":reason})
      return
  }
  res.status(status).json({ds_lists})
  return
});

server.get('/api/v1/get_ds_skeleton_columns_by_ds_id',(req,res)=>{
  const ds_skl=ds_column_headers.dataset_table_skeleton.filter(tbl_skl => tbl_skl.dataset_id === Number(req.headers.dataset_id))
  const status = 200 
  if (!ds_skl)
  {
      const reason = 'No data found.'
      res.status(status).json({ "result":reason})
      return
  }
  res.status(status).json({ds_skl})
  return
});

server.get('/api/v1/get_raw_ds_data_by_id',(req,res)=>{
  const dataset=ds_data.dataset_data.filter(tbl_res => tbl_res.dataset_id === Number(req.headers.dataset_id))
  const status = 200 
  if (!dataset)
  {
      const reason = 'No data found.'
      res.status(status).json({ "result":reason})
      return
  }
  res.status(status).json({ dataset })
  return
});

server.get('/api/v1/getskeleton',(req,res)=>{
  const ds_skl=ds_skeletondb.dataset_skeleton.filter(res => res.dataset_id === Number(req.headers.dataset_id)&&res.status===true)
  const status = 200 
  if (!ds_skl)
  {
      const reason = 'No data found.'
      res.status(status).json({ "result":reason})
      return
  }
  res.status(status).json({ds_skl})
  return
});

server.post('/api/v1/signin', (req, res) => {
  const {username, password} = req.body
  let status = 0
  let reason=""

  const usr=userdb.users.find(user=> user.username=== req.body.username&& user.status===true)
 
  if (!usr)
  {
    status = 200 
    reason = "No user found."
    res.status(status).json({ "auth": false,"jwt_token":null, reason})
    return
  }
  
  if (isAuthenticated({username, password}) === false) {
    status = 200 
    reason = 'User or Password is not matched.'
    console.log("isAuthenticated")
    res.status(status).json({ "auth": false,"jwt_token":null, reason})
    return
  }

  const access_token = createToken({username:usr.username, email:usr.email, fullname:usr.fullname, duty_role:usr.duty_role})
    res.status(200).json({ "auth": true,"jwt_token": access_token})
    return
  })

server.use((req, res, next) => {
  if (req.method === 'POST') {
    req.method = 'GET'
    req.query = req.body
  }
  // Continue to JSON Server router
  next()
})

// Use default router
server.use(router)
server.listen(3000, () => {
  console.log('Fake JSON Server is running')
});