#include <iostream>
#include <vector>
#include <chrono>
#include "omp.h"

using namespace std;

const int arr_dim_size = 20000;

long long parallel_sum(vector<vector<int>>, int);
long long parallel_min(vector<vector<int>>, int);

int main() {
	auto arr = vector<vector<int>>(arr_dim_size, vector<int>(arr_dim_size));
	srand(time(nullptr));

	for (auto& i : arr)
	{
		for (auto& j : i)
		{
			j = rand() % 1240;
		}
	}

	omp_set_nested(1);
	double t1 = omp_get_wtime();
#pragma omp parallel sections
	{
#pragma omp section
		{
			cout << "min 1 = " << parallel_min(arr, 1) << endl;
			cout << "min 2 = " << parallel_min(arr, 2) << endl;
			cout << "min 3 = " << parallel_min(arr, 3) << endl;
			cout << "min 4 = " << parallel_min(arr, 4) << endl;
			cout << "min 8 = " << parallel_min(arr, 8) << endl;
			cout << "min 10 = " << parallel_min(arr, 10) << endl;
			cout << "min 16 = " << parallel_min(arr, 16) << endl;
			cout << "min 32 = " << parallel_min(arr, 32) << endl;
		}

#pragma omp section
		{
			cout << "sum 1 = " << parallel_sum(arr, 1) << endl;
			cout << "sum 2 = " << parallel_sum(arr, 2) << endl;
			cout << "sum 3 = " << parallel_sum(arr, 3) << endl;
			cout << "sum 4 = " << parallel_sum(arr, 4) << endl;
			cout << "sum 8 = " << parallel_sum(arr, 8) << endl;
			cout << "sum 10 = " << parallel_sum(arr, 10) << endl;
			cout << "sum 16 = " << parallel_sum(arr, 16) << endl;
			cout << "sum 32 = " << parallel_sum(arr, 32) << endl;
		}
	}
	double t2 = omp_get_wtime();

	cout << "Total time - " << t2 - t1 << " seconds" << endl;
	return 0;
}

long long parallel_sum(vector<vector<int>> arr, int num_threads) {
	long long sum = 0;
	double t1 = omp_get_wtime();

#pragma omp parallel for reduction(+:sum) num_threads(num_threads)	
	for (int i = 0; i < arr.size(); i++) {
		auto part_sum = 0;

		for (int j = 0; j < arr[i].size(); j++)
		{
			part_sum += arr[i][j];
		}
		sum += part_sum;
	}

	double t2 = omp_get_wtime();

	cout << "(" << t2 - t1 << " seconds)";

	return sum;
}

long long parallel_min(vector<vector<int>> arr, int num_threads) {
	long long min = LLONG_MAX, minIdx = -1;
	double t1 = omp_get_wtime();

	for (int i = 0; i < arr.size(); i++) {
		long long sum = 0;

#pragma omp parallel for reduction(+:sum) num_threads(num_threads)	
		for (int j = 0; j < arr[i].size(); j++)
		{
			sum += arr[i][j];
		}

		if (sum < min)
		{
			min = sum;
			minIdx = i;
		}
	}

	double t2 = omp_get_wtime();

	cout << "(" << t2 - t1 << " seconds)";

	return minIdx;
}