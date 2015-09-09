#ctfeasyserver.rb

require 'sinatra'
require 'digest/sha1'

flag = ENV['flag'] && ENV['flag'].dup || Digest::SHA1.hexdigest('secretflag')
# to be used with signature, will ignore if not set.
signature = ENV['signature'] && ENV['signature'].dup || false

get '/colorserve/:colorcode' do
	if params['colorcode'] == 'roygongerd'
		return '#' + flag
	else
		return '#' + Digest::SHA1.hexdigest(params['colorcode'])
	end
end
