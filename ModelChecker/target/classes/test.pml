int list[100]=0;
bool isactive[17]=false;
statetochange=false;
actioncount=1;
temp1=1;
temp2=1;
int stateNumber=17;
int transNumber=14;
bool cangoout[17]=true;
int i=1;
active proctype ourtype(){
isactive[1]=true;
do{
::(isactive[1] )->atomic{
statetochange=true;
list[i]=1;
i++;
isactive[1]=false;
}
::(isactive[2] )->atomic{
statetochange=true;
list[i]=2;
i++;
isactive[2]=false;
}
::(isactive[2] )->atomic{
statetochange=true;
list[i]=3;
i++;
isactive[2]=false;
}
::(isactive[3] )->atomic{
statetochange=true;
list[i]=4;
i++;
isactive[4]=false;
isactive[9]=false;
isactive[5]=false;
isactive[11]=false;
isactive[3]=false;
isactive[7]=false;
isactive[8]=false;
isactive[6]=false;
isactive[10]=false;
}
::(isactive[5] )->atomic{
statetochange=true;
list[i]=5;
i++;
isactive[5]=false;
}
::(isactive[6] )->atomic{
statetochange=true;
list[i]=6;
i++;
isactive[6]=false;
}
::(isactive[4] &&isactive[3] &&isactive[6] )->atomic{
statetochange=true;
list[i]=7;
i++;
isactive[4]=false;
isactive[9]=false;
isactive[5]=false;
isactive[11]=false;
isactive[3]=false;
isactive[7]=false;
isactive[8]=false;
isactive[6]=false;
isactive[10]=false;
}
::(isactive[9] )->atomic{
statetochange=true;
list[i]=8;
i++;
isactive[9]=false;
}
::(isactive[10] )->atomic{
statetochange=true;
list[i]=9;
i++;
isactive[10]=false;
}
::(isactive[3] &&isactive[8] &&isactive[10] )->atomic{
statetochange=true;
list[i]=10;
i++;
isactive[4]=false;
isactive[9]=false;
isactive[5]=false;
isactive[11]=false;
isactive[3]=false;
isactive[7]=false;
isactive[8]=false;
isactive[6]=false;
isactive[10]=false;
}
::(isactive[12] )->atomic{
statetochange=true;
list[i]=11;
i++;
isactive[12]=false;
isactive[16]=false;
isactive[13]=false;
isactive[15]=false;
isactive[14]=false;
}
::(isactive[13] )->atomic{
statetochange=true;
list[i]=12;
i++;
isactive[13]=false;
}
::(isactive[14] )->atomic{
statetochange=true;
list[i]=13;
i++;
isactive[14]=false;
}
::(isactive[15] )->atomic{
statetochange=true;
list[i]=14;
i++;
isactive[15]=false;
}
else->{
if::(statetochange)->statetochange=false;
i=1;
do::(temp1<stateNumber && cangoout[temp1]==false)->cangoout[temp1]=true;
temp1++;
::(temp1==stateNumber)->break;
::else->temp1++od;
temp1=1;
do::(list[actioncount]==1)->atomic{
actioncount++;
isactive[2]=true;
}
::(list[actioncount]==2)->atomic{
actioncount++;
isactive[4]=true;
isactive[9]=true;
isactive[5]=true;
isactive[3]=true;
isactive[8]=true;
}
::(list[actioncount]==3)->atomic{
actioncount++;
isactive[12]=true;
isactive[14]=true;
}
::(list[actioncount]==4)->atomic{
actioncount++;
isactive[12]=true;
isactive[13]=true;
}
::(list[actioncount]==5)->atomic{
actioncount++;
isactive[6]=true;
}
::(list[actioncount]==6)->atomic{
actioncount++;
isactive[7]=true;
}
::(list[actioncount]==7)->atomic{
actioncount++;
isactive[12]=true;
isactive[13]=true;
}
::(list[actioncount]==8)->atomic{
actioncount++;
isactive[10]=true;
}
::(list[actioncount]==9)->atomic{
actioncount++;
isactive[11]=true;
}
::(list[actioncount]==10)->atomic{
actioncount++;
isactive[12]=true;
isactive[15]=true;
}
::(list[actioncount]==11)->atomic{
actioncount++;
isactive[17]=true;
}
::(list[actioncount]==12)->atomic{
actioncount++;
isactive[14]=true;
}
::(list[actioncount]==13)->atomic{
actioncount++;
isactive[15]=true;
}
::(list[actioncount]==14)->atomic{
actioncount++;
isactive[16]=true;
}
::(list[actioncount]==0)->break;
od;
actioncount=1;
do::(temp2<transNumber && list[temp2]!=0)->list[temp2]=0;
temp2++;
::(temp2==transNumber)->break;
::else->temp2++;
od;
temp2=1;
::else->goto DeadLock;
fi;
}
od;
DeadLock:printf("Dead Lock");
TheEnd:skip;
