# ~/.bashrc: executed by bash(1) for non-login shells.

export PS1='\h:\w\$ '
umask 022

# enable automatic color support where supported
alias ls='ls --color=auto'
alias ip='ip --color=auto'
alias bridge='bridge --color=auto'
alias git='git --color=auto'

# Some more alias to avoid making mistakes:
# alias rm='rm -i'
# alias cp='cp -i'
# alias mv='mv -i'
