import os
import sys
import socket


app_name = "api-gateway"

def git_pull():
    os.system("git pull")

def change_config_file():
    print "changing config file"

    bootstrap_yml = open("src/main/resources/bootstrap.yml", "w")
    bootstrap_yml.truncate()

    env = sys.argv[1] if len(sys.argv) > 1 else "dev"

    if env == "dev":
        env_yml = "bootstrap-dev.yml"
    elif env == "test":
        env_yml = "bootstrap-test.yml"
    elif env == "prod":
        env_yml = "bootstrap-prod.yml"

    env_yml_path = "src/main/resources/" + env_yml
    env_yml_file = open(env_yml_path)

    for line in env_yml_file:
        bootstrap_yml.write(line)

    bootstrap_yml.close()
    env_yml_file.close()

    print "using " + env_yml_path + " as config file"

def package_jar():
    cmd = "mvn clean"
    print "running " + cmd
    os.system(cmd)

    cmd = "mvn package"
    print "running " + cmd
    os.system(cmd)

def build_image():
    docker_tag = "localhost:5555/" + app_name + ":latest"

    os.system("docker build --tag=" + app_name + " --force-rm=true .")
    os.system("docker tag " + app_name + " " + docker_tag)
    os.system("docker push " +  docker_tag)

def k8s_deploy():
    os.system("kubectl delete deployment api-gateway")
    os.system("kubectl create deployment api-gateway --image=localhost:5555/api-gateway:latest")
    app_name

if __name__ == '__main__':
    git_pull()
    change_config_file()
    package_jar()
    build_image()
