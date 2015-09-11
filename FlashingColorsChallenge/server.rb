#ctfeasyserver.rb

require 'sinatra'
require 'digest/sha1'

opts = ENV['opts'] && eval("Hash#{ENV['opts'].split(',').to_s}") || {}
flag = ENV['flag'] && ENV['flag'].dup || Digest::SHA1.hexdigest('secretflag')
# to be used with signature, will ignore if not set.
signature = opts[:signature] || false

get '/colorserve/:colorcode' do
	if params['colorcode'] == 'roygongerd'
		return '#' + flag
	else
		return '#' + Digest::SHA1.hexdigest(params['colorcode'])
	end
end
