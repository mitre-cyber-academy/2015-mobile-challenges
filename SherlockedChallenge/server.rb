#sherlockedserver.rb

require 'sinatra'
require 'digest/sha1'
require 'json'

flag = ENV['flag'] && ENV['flag'].dup || Digest::SHA1.hexdigest('secretflag')
passcode = ENV['passcode'] && ENV['passcode'].dup || 'RUBY'
order = ENV['order'] && ENV['order'].dup || '0123'
# to be used with signature, will ignore if not set.
signature = ENV['signature'] && ENV['signature'].dup || false

get '/passcode' do
	return passcode
end

get '/order' do
	return order
end

post '/challenge' do
	loc_passcode =  params['passcode']
	loc_order = params['order']
	if loc_passcode == passcode && loc_order == order
		content_type :json
  		return { :flag => flag }.to_json
  	else 
  		content_type :json
  		return { :flag => 'You didnt say the magic word' }.to_json
  	end
end