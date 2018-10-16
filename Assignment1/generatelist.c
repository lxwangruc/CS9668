#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main(){
        time_t t;
        srand((unsigned) time(&t));
        FILE* output = fopen("list.txt","w");
        int i = 0, j = 0;
        for(i = 0; i <100; i ++){
        //      for(j = 0; j < 256; j ++){
                fprintf(output,"129.100.%d.%d\n",rand()%256,rand()%256);
        //      }
        }
        return 0;
}
