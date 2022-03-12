# List plugins dependencies
plugins_dependencies = %w( vagrant-docker-compose )

plugin_status = false
plugins_dependencies.each do |plugin_name|
  unless Vagrant.has_plugin? plugin_name
    system("vagrant plugin install #{plugin_name}")
    plugin_status = true
    puts " #{plugin_name}  Dependencies installed"
  end
end

# Restart Vagrant if any new plugin installed
if plugin_status === true
  exec "vagrant #{ARGV.join' '}"
end

Vagrant.configure("2") do |config|
    config.vm.box = "genebean/centos-7-docker-ce" #lpf/centos7docker
    config.vm.hostname = "expman-vagrant"
    if Vagrant.has_plugin?("vagrant-vbguest") then
          config.vbguest.auto_update = false
    end
    config.vm.provider "virtualbox" do |v|
        v.name = "expman-vagrant"
        v.memory = 2048
        v.cpus = 2
    end

    #network
    config.vm.network "forwarded_port", host: 15432, guest: 5432, id: "postgres"
    config.vm.network "forwarded_port", host: 27017, guest: 27017, id: "mongo"

    #shell
    config.vm.provision :docker_compose, yml: ["/vagrant/.docker/db/docker-compose.yml"], run: "always"
end