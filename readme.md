i should probably store table data as a linked list
metadata - 
pagesize - dynamic, default should be 8096 tho
probably single table rn
file strcutre will be like this
database metadata -> tables -> table indexes and stuff -> root node
# page architecture
size (bytes ) | disc | other note 
# format type
16 | string describing format | "Snack Database"  (14bytes but its okay)
# header page
4 | page size
8 | free page list start
2 | amount of indexes
2 * indexCount | pointers to b tree configs

# table meta data
//for now its a single table, but i guess i could like have a table pointing to tables or do the b tree thing but idk
4 | total pages
4 | total rows
4 | amount of columns in a key (can be multi column) | for parsing
4 | amount of columns in the table | for parsing
2 * keyCount |  key types and sizes | first byte describes type, second describes size ( max column name size is 256)
2 * valueCount | first byte describes value type, second describes value size ( max of 256)
8 | pointer to root node page
# internal page
headersize - 6 bytes
0 2 page type
2 6 for page capacity 
data will be stored like
[value][key][value][key]...

# leaf
headersize - 22 bytes
0 2 page type
2 10 prev page 
10 18 next page
18 22 capacity
data will be stored like
[key][value][key][value]

# free pool page - 
headersize - 6 bytes