#ctfhardserver.rb

require 'sinatra'
require 'pg'
require 'digest/sha1'
require_relative 'populate_userpls'

opts = ENV['opts'] && eval("Hash#{ENV['opts'].split(',').to_s}") || {}
flag = ENV['flag'] && ENV['flag'].dup || Digest::SHA1.hexdigest('secretflag')
# to be used with signature, will ignore if not set.
signature = opts[:signature] || false
dbname = opts[:dbname] || 'ctfharddb'
dbuser = opts[:dbuser] || 'testuser'
dbpass = opts[:dbpass] || 'masthead'
adminpass = opts[:adminpass] || 'password'


set :server, 'thin'
set :public_folder, 'frontend'
set :session_secret, 'iBfOUzow31KD'

db_params = {
  host: 'localhost',
  dbname: dbname,
  user: dbuser,
  password: dbpass
}

$current_uid

configure do
	enable :sessions
	begin
		psql = PG::Connection.new(db_params)
		psql.exec_params("CREATE TABLE IF NOT EXISTS users(username text, password text, uid BIGSERIAL PRIMARY KEY)")
		psql.exec_params("CREATE TABLE IF NOT EXISTS playlists(uid BIGINT REFERENCES users(uid), song text, artist text, sid BIGSERIAL)")
		begin
			psql.exec_params("SELECT * FROM users WHERE username='admin'").getvalue(0,0)
		rescue ArgumentError
			#adminpass = Digest::SHA1.hexdigest(rand(999999).to_s)[0,6]
			#flag = Digest::SHA1.hexdigest('teamnumbersflag')
			psql.exec_params("INSERT INTO users VALUES ('admin', $1)", [adminpass])
			psql.exec_params("INSERT INTO playlists VALUES (1, $1, 'flag')", [flag])
			psql.close();
		end
		adduserplaylists(db_params)
	end
end

helpers do
	def logged_in?
		not @user.nil?
	end
end

before do
	@user = session[:user]
end

get '/error' do
	send_file File.expand_path('error.html', settings.public_folder)
end

get '/' do
	send_file File.expand_path('login.html', settings.public_folder)
end

get '/login' do
	session[:user] = nil
	send_file File.expand_path('login.html', settings.public_folder)
end

post '/login' do
	psql = PG::Connection.new(db_params)
	begin
		if !(params['username'].length == 0 || params['password'].length == 0) && params['password'] == psql.exec_params("SELECT password FROM users WHERE username=$1", [params['username']]).getvalue(0,0)
			session[:user] = params['password']
			$current_uid = psql.exec_params("SELECT uid FROM users WHERE username=$1", [params['username']]).getvalue(0,0)
			psql.close()
			redirect to ('playlist')
		else
			session[:user] = nil
			psql.close()
			redirect to ('login')
		end
	rescue ArgumentError
		if params['username'].length != 0 && params['password'].length != 0
			psql.exec_params("INSERT INTO users VALUES ($1, $2)", [params['username'], params['password']])
			session[:user] = params['password']
			$current_uid = psql.exec("SELECT uid FROM users WHERE username=$1", [params['username']]).getvalue(0,0)
			psql.close()
			redirect to ('playlist')
		else
			session[:user] = nil
			psql.close()
			redirect to ('login')
		end
	end
end

get '/logout' do
	session[:user] = nil
	redirect to ('login')
end

get '/playlist' do
	if session[:user] != nil
		send_file File.expand_path('playlist.html', settings.public_folder)
	else
		redirect to ('login')
	end
end

get '/playlist-remove' do
	if session[:user] != nil
		send_file File.expand_path('playlist-remove.html', settings.public_folder)
	else
		redirect to ('login')
	end
end

post '/playlist-remove' do
	psql = PG::Connection.new(db_params)
	psql.exec_params("DELETE FROM playlists WHERE sid = $1", [params['sid']])
	psql.close()
	redirect to ('playlist-remove-update')
end

get '/playlist-remove-update' do
	if session[:user] != nil
		redirect to ('playlist-remove')
	else
		redirect to ('login')
	end
end

get '/playlist-add' do
	if session[:user] != nil
		send_file File.expand_path('playlist-add.html', settings.public_folder)
	else
		redirect to ('login')
	end
end

post '/playlist-add' do
	psql = PG::Connection.new(db_params)
	begin
		if params['song'] == psql.exec_params("SELECT song FROM playlists WHERE uid = $1 AND song = $2 AND artist = $3", [($current_uid).to_s, params['song'], params['artist']]).getvalue(0,0)
			psql.close()
			redirect to ('playlist-add')
		end
	rescue ArgumentError
		if params['song'] != ""
			psql.exec_params("INSERT INTO playlists VALUES ($1, $2, $3)", [($current_uid).to_s, params['song'], params['artist']])
			psql.close()
			redirect to ('playlist-add')
		else
			psql.close()
			redirect to ('playlist-add')
		end
	end
end

post '/update-playlist' do
	psql = PG::Connection.new(db_params)
	jplaylist = "{\"playlist\":["
	res = psql.exec_params("SELECT * FROM playlists WHERE uid = $1", [($current_uid).to_s])
	res.each_row do |row|
		jplaylist += "{\"song\":\""+row[1]+"\", \"artist\":\""+row[2]+"\", \"sid\":\""+row[3]+"\"},"
	end
	jplaylist = jplaylist.chop
	jplaylist+="]}"
	return jplaylist
end

post '/get-pass' do
	psql = PG::Connection.new(db_params)
	begin
		return psql.exec_params("SELECT password FROM users WHERE username = $1", [params['username']]).getvalue(0,0)
			psql.close()
	rescue ArgumentError
		psql.close()
		return ''
	end
end

#get '/search-users' do
#	if session[:user] != nil
#		send_file File.expand_path('search-users.html', settings.public_folder)
#	else
#		redirect to ('login')
#	end
#end

#get '/other-users' do
#	if session[:user] != nil
#		send_file File.expand_path('other-users.html', settings.public_folder)
#	else
#		redirect to ('login')
#	end
#end

#get '/other-playlist' do
#	if session[:user] != nil
#		send_file File.expand_path('other-playlists.html', settings.public_folder)
#	else
#		redirect to ('login')
#	end
#end
