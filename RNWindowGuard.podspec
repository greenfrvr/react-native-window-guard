require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "RNWindowGuard"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  RNWindowGuard
                   DESC
  s.homepage     = "https://github.com/author/RNWindowGuard"
  s.license      = package["license"]
  # s.license    = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author       = { "author" => package["author"]["email"] }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "#{package["repository"]["baseUrl"]}.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m}"
  s.requires_arc = true

  s.dependency "React"
  #s.dependency "others"
end

